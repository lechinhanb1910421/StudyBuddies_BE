package com.everett.daos;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.exceptions.checkedExceptions.EmptyCommentException;
import com.everett.exceptions.checkedExceptions.NotificationNotFoundException;
import com.everett.models.Notification;

public class UserNotificationDAO {
    private static final Logger logger = LogManager.getLogger(CommentDAO.class);

    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public List<Notification> getUserNotificationsByEmail(String email) throws EmptyCommentException {
        logger.info("GET ALL NOTIFICATIONS FOR USER ID: " + email);

        List<Notification> res = null;
        try {
            TypedQuery<Notification> query = entityManager
                    .createQuery(
                            "FROM Notifications n LEFT JOIN FETCH n.receiverUser u WHERE n.receiverUser.email = :email ORDER BY n.createdAt DESC",
                            Notification.class);
            res = query.setParameter("email", email).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res == null) {
            throw new EmptyCommentException();
        } else {
            return res;
        }

    }

    public Notification getNotificationById(Long notiId) throws NotificationNotFoundException {
        try {
            return Optional.ofNullable(entityManager.find(Notification.class, notiId))
                    .orElseThrow(() -> new NotificationNotFoundException(notiId));
        } catch (Exception ex) {
            logger.info("CANNOT FIND NOTIFICATION WITH ID: " + notiId);
            throw new NotificationNotFoundException(notiId);
        }
    }

    public void persistNotfication(Notification notification) {
        try {
            entityManager.persist(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNotification(Notification notification) {
        try {
            entityManager.merge(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
