package com.everett.apis;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.checkedExceptions.UserPersistedException;
import com.everett.models.Message;
import com.everett.models.User;
import com.everett.services.PostService;
import com.everett.services.UserService;

@Path("/users")
@ApplicationScoped
@DenyAll
public class UserAPI {
    private static final Logger logger = LogManager.getLogger(UserAPI.class);
    @Inject
    UserService userService;

    @Inject
    PostService postService;

    @Inject
    JsonWebToken jwt;

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
    public Response addUserFromContext() {
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        User user = new User(loginName, givenName, familyName, email, createdTime, "active");
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

    @Path("/{user_mail}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getUserByEmail(@PathParam("user_mail") String user_mail) {
        try {
            System.out.println("GET USER WITH MAIL: " + user_mail);
            return Response.ok(userService.getUserByEmail(user_mail)).build();
        } catch (UserNotFoundException e) {
            Message message = new Message("User with email " + user_mail + " not found");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
    }

    @Path("/posts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response getAllUserPost() {
        logger.info("GET ALL USER POSTS");
        return Response.ok(postService.getAllUserPosts(email)).build();
    }
}
