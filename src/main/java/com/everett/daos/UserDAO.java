package com.everett.daos;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.User;

@Stateless
public class UserDAO {
    private static final Logger logger = LogManager.getLogger(UserDAO.class);

    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public User getUserById(Long id) throws UserNotFoundException {
        User user = null;
        try {
            logger.info("GETTING USER INFO WITH ID " + id);
            user = entityManager.find(User.class, id);
        } catch (Exception e) {
            throw new UserNotFoundException(id);
        }
        if (user == null) {
            throw new UserNotFoundException(id);
        } else {
            return user;
        }
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = null;
        try {
            logger.info("GETTING USER INFO WITH MAIL: " + email);
            TypedQuery<User> query = entityManager.createQuery("FROM Users s WHERE s.email = :email", User.class);
            user = query.setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException(email);
        }
        return user;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistUser(User user) {
        try {
            entityManager.persist(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> resList = null;
        try {
            TypedQuery<User> userQuery = entityManager.createQuery("FROM Users s ORDER BY s.userId", User.class);
            resList = userQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }

    public void updateUser(User user) {
        try {
            entityManager.merge(user);
        } catch (Exception e) {
        }
    }

    public Long getCountUsers() {
        Long count = null;
        try {
            TypedQuery<Long> query = entityManager
                    .createQuery("SELECT COUNT(*) FROM Users s",
                            Long.class);
            count = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public Integer getUserPostsCount(Long userId) throws UserNotFoundException {
        User user = getUserById(userId);
        return user.getPosts().size();

    }
}
