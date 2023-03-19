package com.everett.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.everett.daos.UserDAO;
import com.everett.dtos.UserResponseDTO;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.checkedExceptions.UserPersistedException;
import com.everett.models.Avatar;
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
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userDAO.getAllUsers();
        List<UserResponseDTO> results = new ArrayList<UserResponseDTO>();
        users.forEach(user -> {
            UserResponseDTO userRes = new UserResponseDTO(user);
            Set<Avatar> avatarSet = user.getAvatars();
            avatarSet.forEach(avatar -> {
                userRes.setSingleAvatar(avatar.getAvaUrl());
            });
            results.add(userRes);
        });
        return results;
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        return userDAO.getUserByEmail(email);
    }

    @Override
    public UserResponseDTO getUserById(Long userId) throws UserNotFoundException {
        User user = userDAO.getUserById(userId);
        UserResponseDTO userRes = new UserResponseDTO(user);
        Set<Avatar> avatarSet = user.getAvatars();
        avatarSet.forEach(avatar -> {
            userRes.setSingleAvatar(avatar.getAvaUrl());
        });
        return userRes;
    }

    @Override
    public void addUserAvatar(String userEmail, Avatar avatar) throws UserNotFoundException {
        User user = userDAO.getUserByEmail(userEmail);
        user.setAvatar(avatar);
        userDAO.updateUser(user);
    }

    @Override
    public void removeUserAvatar(String userEmail, String avatarUrl) throws UserNotFoundException {
        User user = userDAO.getUserByEmail(userEmail);
        Set<Avatar> avatarSet = user.getAvatars();
        avatarSet.forEach((elem) -> {
            if (elem.getAvaUrl() == avatarUrl) {
                System.out.println("FOUND AVATAR " + avatarUrl);
                elem.setUser(null);
                // avatarSet.remove(elem);
                user.unsetAvatar(elem);
            }
        });
        user.setAvatarsSet(avatarSet);
        userDAO.updateUser(user);
    }
}
