package com.everett.apis;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
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
import com.everett.exceptions.checkedExceptions.NotificationNotFoundException;
import com.everett.exceptions.checkedExceptions.UserNoPermissionException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.checkedExceptions.UserPersistedException;
import com.everett.models.Avatar;
import com.everett.models.Message;
import com.everett.models.User;
import com.everett.models.type.UserRoleType;
import com.everett.services.PostService;
import com.everett.services.UserNotificationService;
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
    @Claim("groups")
    private Instance<Set<String>> roleGroups;

    @Inject
    @ConfigProperty(name = "default_ava_url")
    private String defaultAvaUrl;

    @Inject
    private UserNotificationService notificationService;

    @Path("/whoami")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getUserFromContext() {
        try {
            System.out.println("GET USER WITH MAIL: " + email);
            return Response.ok(userService.getUserResByEmail(email)).build();
        } catch (UserNotFoundException e) {
            Message message = new Message("User with email " + email + " not found");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response addUserFromContext(@QueryParam("avaUrl") String avaUrl) {
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        UserRoleType role = this.getUserRole();
        User user = new User(loginName, givenName, familyName, email, createdTime, "active", role);
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

    private UserRoleType getUserRole() {
        Set<String> roles = roleGroups.iterator().next();
        if (roles.contains(UserRoleType.ADMIN.name())) {
            return UserRoleType.ADMIN;
        } else if (roles.contains(UserRoleType.TEACHER.name())) {
            return UserRoleType.TEACHER;
        } else {
            return UserRoleType.STUDENT;
        }
    }

    @Path("/")
    @GET
    @RolesAllowed("ADMIN")
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

    @Path("/{userId}/posts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getAllUserPost(@PathParam("userId") Long userId) {
        logger.info("GET ALL USER POSTS");
        try {
            return Response.ok(postService.getAllUserPosts(userId)).build();
        } catch (UserNotFoundException e) {
            logger.info("USER WITH ID: " + userId + " NOT FOUND");
            Message message = new Message("User with id: " + userId + " not found");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
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

    @Path("/notifications")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ADMIN", "STUDENT", "TEACHER" })
    public Response getUserNotification() {
        logger.info("GET NOTIFICATION FOR USER EMAIL: [" + email + "]");
        try {
            return Response.ok(notificationService.getUserNotificationByEmail(email)).build();
        } catch (UserNotFoundException e) {
            logger.error("USER WITH EMAIL " + email + " NOT FOUND");
            Message errMessage = new Message("User with email " + email + " not found");
            throw new WebApplicationException(Response.status(400).entity(errMessage).build());
        }
    }

    @Path("/notifications/{notiId}")
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ADMIN", "STUDENT", "TEACHER" })
    public Response setNotificationState(@PathParam("notiId") Long notiId) {
        logger.info("SET READ STATUS FOR NOTIFICATION ID: [" + notiId + "]");
        try {
            notificationService.setNotifitionReadStateById(notiId, email);
            return Response.ok().build();
        } catch (NotificationNotFoundException e) {
            logger.error("NOTIFICATION WITH ID " + notiId + " NOT FOUND");
            Message errMessage = new Message("Notification with id " + notiId + " not found");
            throw new WebApplicationException(Response.status(400).entity(errMessage).build());
        } catch (UserNoPermissionException e) {
            logger.error("USER WITH EMAIL " + email + " HAS NOT PERMISSTION TO CHANGE NOTIFICATION ID: " + notiId);
            Message errMessage = new Message(
                    "User with email " + email + " has not permisstion to change notification id " + notiId);
            throw new WebApplicationException(Response.status(400).entity(errMessage).build());
        } catch (UserNotFoundException e) {
            logger.error("USER WITH EMAIL " + email + " NOT FOUND");
            Message errMessage = new Message("User with email " + email + " not found");
            throw new WebApplicationException(Response.status(400).entity(errMessage).build());
        }
    }

}
