package com.everett.services;

import java.util.List;

import com.everett.dtos.UserResponseDTO;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.checkedExceptions.UserPersistedException;
import com.everett.models.Avatar;
import com.everett.models.User;

public interface UserService {

    public void persistContextUser(User user) throws UserPersistedException;

    public List<UserResponseDTO> getAllUsers();

    public UserResponseDTO getUserById(Long userId) throws UserNotFoundException;

    public User getUserByEmail(String email) throws UserNotFoundException;

    public void addUserAvatar(String userEmail, Avatar avatar) throws UserNotFoundException;

    public void removeUserAvatar(String userEmail, String avatarUrl) throws UserNotFoundException;
}
