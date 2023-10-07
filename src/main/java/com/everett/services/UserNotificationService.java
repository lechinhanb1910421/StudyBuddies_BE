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
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Avatar;
import com.everett.models.Notification;
import com.everett.models.Post;
import com.everett.models.User;
import com.everett.models.type.NotificationType;

@Stateless
public class UserNotificationService {
    private static final String NOTIFY_CMT_MSG_TEMPLATE = "%s just left a comment on your post";
    private static final String NOTIFY_REACT_MSG_TEMPLATE = "%s just reacted your post";
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
                User user = entry.getReceiverUser();
                UserResponseDTO userDTO = new UserResponseDTO(user);
                userDTO.setAvatars(getUserActiveAvatar(user));
                UserNotificationDTO notiDTO = new UserNotificationDTO(entry);
                notiDTO.setReceiverUser(userDTO);
                result.add(notiDTO);
            });
        } catch (EmptyCommentException e) {
            logger.info("USER WITH EMAIL: " + email + " HAS NO NOTIFICATION YET");
        }
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addNotificationForUser(String senderEmail, NotificationType notificationType, Post post)
            throws UserNotFoundException {
        Long postId = post.getPostId();
        String receiverEmail = post.getOwnerUser().getEmail();
        User senderUser = userService.getUserByEmail(senderEmail);
        User receiverUser = userService.getUserByEmail(receiverEmail);
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        String notiMessage = buildNotifiMessage(notificationType, senderUser);
        String referenceLink = buildNotiReperenceLink(postId);

        Notification notification = new Notification(receiverUser, notiMessage, notificationType.name(), createdTime, referenceLink);
        try {
            notificationDAO.persistNotfication(notification);
        } catch (Exception ex) {
            logger.error("CANNOT ADD NOTFICATION FOR USER: " + senderEmail);
        }
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
