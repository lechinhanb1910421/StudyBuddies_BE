package com.everett.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.everett.daos.AvatarDAO;
import com.everett.daos.UserDAO;
import com.everett.dtos.UserResponseDTO;
import com.everett.exceptions.checkedExceptions.AvatarNotFoundException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.checkedExceptions.UserPersistedException;
import com.everett.models.Avatar;
import com.everett.models.User;

@Stateless
public class UserServiceImp implements UserService {
    @Inject
    UserDAO userDAO;

    @Inject
    AvatarDAO avatarDAO;

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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userDAO.getAllUsers();
        List<UserResponseDTO> results = new ArrayList<UserResponseDTO>();
        users.forEach(user -> {
            UserResponseDTO userRes = new UserResponseDTO(user);
            userRes.setAvatars(avatarDAO.getAllAvatarsByUserId(user.getUserId()));
            results.add(userRes);
        });
        return results;
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        return userDAO.getUserByEmail(email);
    }

    @Override
    public UserResponseDTO getUserResByEmail(String email) throws UserNotFoundException {
        User user = userDAO.getUserByEmail(email);
        UserResponseDTO userResponse = new UserResponseDTO(user);
        userResponse.setAvatars(new ArrayList<Avatar>(user.getAvatars()));
        return userResponse;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public UserResponseDTO getUserById(Long userId) throws UserNotFoundException {
        User user = userDAO.getUserById(userId);
        UserResponseDTO userRes = new UserResponseDTO(user);
        userRes.setAvatars(avatarDAO.getAllAvatarsByUserId(userId));
        return userRes;
    }

    @Override
    public void addUserAvatar(String userEmail, Avatar avatar) throws UserNotFoundException {
        User user = userDAO.getUserByEmail(userEmail);
        user.setAvatar(avatar);
        userDAO.updateUser(user);
    }

    @Override
    public void removeAvatarById(Long avaId) throws AvatarNotFoundException {
        avatarDAO.removeAvatarById(avaId);
    }
}
