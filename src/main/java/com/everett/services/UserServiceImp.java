package com.everett.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;

import com.everett.daos.UserDAO;
import com.everett.exceptions.UserNotFoundException;
import com.everett.exceptions.UserPersistedException;
import com.everett.models.User;

@Stateless
public class UserServiceImp implements UserService {
    @Inject
    UserDAO userDAO;

    @Override
    @SuppressWarnings("rawtypes")
    public User getUserFromContext(SecurityContext securityContext) {
        KeycloakPrincipal principal = (KeycloakPrincipal) securityContext.getUserPrincipal();
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        User user = new User(accessToken.getPreferredUsername(), accessToken.getGivenName(),
                accessToken.getFamilyName(), accessToken.getEmail(), createdTime, "active");
        return user;
    }

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
