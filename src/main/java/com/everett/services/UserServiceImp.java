package com.everett.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.everett.daos.UserDAO;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.checkedExceptions.UserPersistedException;
import com.everett.models.User;

@Stateless
public class UserServiceImp implements UserService {
    @Inject
    UserDAO userDAO;

    @Override
    public void persistContextUser(User user) throws UserPersistedException {
        try {
            userDAO.getUserByEmail(user.getEmail());
            throw new UserPersistedException();
        } catch (UserNotFoundException e) {
            userDAO.persistUser(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        return userDAO.getUserByEmail(email);
    }
}
