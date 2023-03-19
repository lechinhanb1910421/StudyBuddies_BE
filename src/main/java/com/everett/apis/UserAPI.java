package com.everett.apis;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claim;

import com.everett.dtos.UserResponseDTO;
import com.everett.exceptions.checkedExceptions.AvatarNotFoundException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.checkedExceptions.UserPersistedException;
import com.everett.models.Avatar;
import com.everett.models.Message;
import com.everett.models.User;
import com.everett.services.PostService;
import com.everett.services.UserService;

@Path("/users")
@RequestScoped
@DenyAll
public class UserAPI {
    private static final Logger logger = LogManager.getLogger(UserAPI.class);
    @Inject
    UserService userService;

    @Inject
    PostService postService;

    @Inject
    @Claim("given_name")
    private String givenName;

    @Inject
    @Claim("family_name")
    private String familyName;

    @Inject
    @Claim("email")
    private String email;

    @Inject
    @Claim("preferred_username")
    private String loginName;

    @Inject
    @ConfigProperty(name = "default_ava_url")
    private String defaultAvaUrl;

    @Path("/whoami")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getUserFromContext() {
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        User user = new User(loginName, givenName, familyName, email, createdTime, "active");
        return Response.ok(user).build();
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response addUserFromContext(@QueryParam("avaUrl") String avaUrl) {
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        User user = new User(loginName, givenName, familyName, email, createdTime, "active");
        Avatar avatar = null;
        if (avaUrl == null) {
            avatar = new Avatar(defaultAvaUrl);
        } else {
            avatar = new Avatar(avaUrl);
        }
        user.setAvatar(avatar);
        Message message = null;
        try {
            userService.persistContextUser(user);
            message = new Message("User was successfully persisted");
            return Response.ok(message).build();
        } catch (UserPersistedException e) {
            message = new Message("User was already persisted");
            return Response.ok(message).build();
        }
    }

    @Path("/")
    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        return Response.ok(userService.getAllUsers()).build();
    }

    @Path("/id/{userId}")
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("userId") Long userId) {
        try {
            return Response.ok(userService.getUserById(userId)).build();
        } catch (UserNotFoundException e) {
            Message message = new Message("User with id " + userId + " not found");
            return Response.status(400).entity(message).build();
        }
    }

    @Path("/email/{user_mail}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getUserByEmail(@PathParam("user_mail") String user_mail) {
        try {
            System.out.println("GET USER WITH MAIL: " + user_mail);
            User user = userService.getUserByEmail(user_mail);
            UserResponseDTO userResponse = new UserResponseDTO(user);
            userResponse.setAvatars(new ArrayList<Avatar>(user.getAvatars()));
            return Response.ok(userResponse).build();
        } catch (UserNotFoundException e) {
            Message message = new Message("User with email " + user_mail + " not found");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
    }

    @Path("/posts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getAllUserPost() {
        logger.info("GET ALL USER POSTS");
        return Response.ok(postService.getAllUserPosts(email)).build();
    }

    @Path("/avatars")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response addUserAvatar(Avatar avatar) {
        logger.info("ADD AVATAR URL: [" + avatar.getAvaUrl() + "] FOR USER: [" + email + "]");
        try {
            userService.addUserAvatar(email, avatar);
            Message message = new Message(
                    "Avatar Url: [" + avatar.getAvaUrl() + "] was successfully add for user: [" + email + "]");
            return Response.ok(message).build();
        } catch (UserNotFoundException e) {
            logger.error("USER WITH EMAIL " + email + " NOT FOUND");
            Message errMessage = new Message("User with email " + email + " not found");
            throw new WebApplicationException(Response.status(400).entity(errMessage).build());
        }
    }

    @Path("/avatars/{avaId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response removeUserAvatar(@PathParam("avaId") Long avaId) {
        logger.info("REMOVE AVATAR ID: [" + avaId + "FOR USER: [" + email + "]");
        try {
            userService.removeAvatarById(avaId);
        } catch (AvatarNotFoundException e) {
            Message err = new Message("Avatar ID: [" + avaId + "] not exists");
            throw new WebApplicationException(Response.status(400).entity(err).build());
        }
        Message message = new Message("Avatar ID: [" + avaId + "] was successfully remove for user:  [" + email + "]");
        return Response.ok(message).build();
    }

}
