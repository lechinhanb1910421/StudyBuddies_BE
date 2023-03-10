package com.everett.services;

import java.util.List;

import javax.ws.rs.core.SecurityContext;

import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.checkedExceptions.UserPersistedException;
import com.everett.models.User;

public interface UserService {
    public User getUserFromContext(SecurityContext securityContext);
    
    public void persistContextUser(User user) throws UserPersistedException;

    public List<User> getAllUsers();

    public User getUserByEmail(String email) throws UserNotFoundException;
}
