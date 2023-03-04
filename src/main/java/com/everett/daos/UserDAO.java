package com.everett.daos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.everett.exceptions.UserNotFoundException;
import com.everett.models.User;

@Stateless(name = "UserDAO")
public class UserDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public User getUserById(Long id) throws UserNotFoundException {
        User user = null;
        try {
            user = entityManager.find(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
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
            TypedQuery<User> query = entityManager.createQuery("FROM Users s WHERE s.email = :email", User.class);
            user = query.setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException(email);
        }
        return user;
    }

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
}
