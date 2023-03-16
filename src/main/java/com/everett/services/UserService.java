package com.everett.services;

import java.util.List;


import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.checkedExceptions.UserPersistedException;
import com.everett.models.User;

public interface UserService {
    
    public void persistContextUser(User user) throws UserPersistedException;

    public List<User> getAllUsers();

    public User getUserByEmail(String email) throws UserNotFoundException;
}
