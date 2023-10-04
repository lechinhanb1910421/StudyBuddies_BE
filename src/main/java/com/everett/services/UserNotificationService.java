package com.everett.services;

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
import com.everett.models.User;

@Stateless
public class UserNotificationService {
    private static final Logger logger = LogManager.getLogger(UserNotificationService.class);

    @Inject
    UserNotificationDAO notificationDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    UserService userService;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<UserNotificationDTO> getUserNotification(Long userId) throws UserNotFoundException {
        userDAO.getUserById(userId);
        List<UserNotificationDTO> result = new ArrayList<UserNotificationDTO>();

        try {
            List<Notification> notifications = notificationDAO.getUserNotificationsById(userId);
            notifications.forEach(entry -> {
                User user = entry.getReceiverUser();
                UserResponseDTO userDTO = new UserResponseDTO(user);
                userDTO.setAvatars(getUserActiveAvatar(user));
                UserNotificationDTO notiDTO = new UserNotificationDTO(entry);
                notiDTO.setReceiverUser(userDTO);
                result.add(notiDTO);
            });
        } catch (EmptyCommentException e) {
            logger.info("USER WITH ID: " + userId + " HAS NO NOTIFICATION YET");
        }
        return result;
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
