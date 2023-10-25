package com.everett.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.daos.UserDAO;
import com.everett.daos.UserNotificationDAO;
import com.everett.dtos.UserNotificationDTO;
import com.everett.dtos.UserResponseDTO;
import com.everett.exceptions.checkedExceptions.EmptyCommentException;
import com.everett.exceptions.checkedExceptions.NotificationNotFoundException;
import com.everett.exceptions.checkedExceptions.UserNoPermissionException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Avatar;
import com.everett.models.Notification;
import com.everett.models.Post;
import com.everett.models.User;
import com.everett.models.type.NotificationStateType;
import com.everett.models.type.NotificationType;

@Stateless
public class UserNotificationService {
    private static final String NOTIFY_CMT_MSG_TEMPLATE = "%s left a comment on your post";
    private static final String NOTIFY_REACT_MSG_TEMPLATE = "%s reacted your post";
    private static final String NOTIFY_REFERENCE_LINK = "http://localhost/post/%s";
    private static final Logger logger = LogManager.getLogger(UserNotificationService.class);

    @Inject
    UserNotificationDAO notificationDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    UserService userService;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<UserNotificationDTO> getUserNotificationByEmail(String email) throws UserNotFoundException {
        userDAO.getUserByEmail(email);
        List<UserNotificationDTO> result = new ArrayList<UserNotificationDTO>();

        try {
            List<Notification> notifications = notificationDAO.getUserNotificationsByEmail(email);
            notifications.forEach(entry -> {
                UserNotificationDTO notiDTO = new UserNotificationDTO(entry);
                notiDTO.setSourceUser(buildSourceUserResponse(entry.getSourceUserId()));
                result.add(notiDTO);
            });
        } catch (EmptyCommentException e) {
            logger.info("USER WITH EMAIL: " + email + " HAS NO NOTIFICATION YET");
        }
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private UserResponseDTO buildSourceUserResponse(Long userId) {
        User user;
        try {
            user = userDAO.getUserById(userId);
        } catch (Exception e) {
            logger.error("CANNOT FIND SOURCE USER IN NOTIFICATION DTO");
            return new UserResponseDTO();
        }
        UserResponseDTO userDTO = new UserResponseDTO(user);
        userDTO.setAvatars(getUserActiveAvatar(user));
        return userDTO;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addNotificationForUser(String senderEmail, NotificationType notificationType, Post post)
            throws UserNotFoundException {
        Long postId = post.getPostId();
        User sendUser = userService.getUserByEmail(senderEmail);
        User receiveUser = getReceiveUser(post);
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        String notiMessage = buildNotifiMessage(notificationType, sendUser);
        String referenceLink = buildNotiReperenceLink(postId);

        Notification notification = new Notification(receiveUser, sendUser.getUserId(), notiMessage,
                notificationType.name(), createdTime,
                referenceLink, NotificationStateType.UNREAD);
        try {
            notificationDAO.persistNotfication(notification);
        } catch (Exception ex) {
            logger.error("CANNOT ADD NOTFICATION FOR USER: " + senderEmail);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void setNotifitionReadStateById(Long notiId, String ownerEmail)
            throws NotificationNotFoundException, UserNoPermissionException, UserNotFoundException {
        try {
            Notification notification = notificationDAO.getNotificationById(notiId);
            User notiOwner = userDAO.getUserByEmail(ownerEmail);
            if (!isUserOwnerOfNotification(notification, notiOwner)) {
                throw new UserNoPermissionException();
            }
            if (notification.getReadStatus() != NotificationStateType.UNREAD) {
                logger.info(String.format("NOTIFICATION WITH ID: %d HAS STATUS: [%s]. NO CHANGES WAS MADE",
                        notiId, notification.getReadStatus()));
                return;
            }
            notification.setReadStatus(NotificationStateType.READ);
            notificationDAO.updateNotification(notification);
        } catch (NotificationNotFoundException e) {
            logger.info("STATUS OF NOTIFICATION WITH ID: " + notiId + " WAS NOT CHANGED");
            throw new NotificationNotFoundException();
        } catch (UserNotFoundException e) {
            logger.info("USER WITH EMAIL: " + ownerEmail + " NOT FOUND");
            throw new UserNotFoundException();

        }

    }

    private boolean isUserOwnerOfNotification(Notification notification, User user) {
        return notification.getReceiverUser().getUserId() == user.getUserId();
    }

    private User getReceiveUser(Post post) throws UserNotFoundException {
        return Optional.ofNullable(post)
                .map(Post::getOwnerUser)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private String buildNotiReperenceLink(Long postId) {
        return String.format(NOTIFY_REFERENCE_LINK, postId);
    }

    private String buildNotifiMessage(NotificationType type, User user) {
        String userName = user.getGivenName() + " " + user.getFamilyName();
        switch (type) {
            case NEW_COMMENT:
                return String.format(NOTIFY_CMT_MSG_TEMPLATE, userName);
            case NEW_REACTION:
                return String.format(NOTIFY_REACT_MSG_TEMPLATE, userName);
            default:
                return "You have received a notification";
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private List<Avatar> getUserActiveAvatar(User user) {
        List<Avatar> result = new ArrayList<>();
        Iterator<Avatar> avatars = Optional.ofNullable(user.getAvatars())
                .orElse(new HashSet<>())
                .iterator();
        while (avatars.hasNext()) {
            Avatar avatar = avatars.next();
            if (avatar.getIsActive() == 1) {
                result.add(avatar);
                return result;
            }
        }
        return result;
    }
}
