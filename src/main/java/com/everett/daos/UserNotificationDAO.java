package com.everett.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.exceptions.checkedExceptions.EmptyCommentException;
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

    public void persistNotfication(Notification notification) {
        try {
            entityManager.persist(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
