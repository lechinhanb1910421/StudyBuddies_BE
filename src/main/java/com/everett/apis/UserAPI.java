package com.everett.apis;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;

import com.everett.exceptions.UserNotFoundException;
import com.everett.exceptions.UserPersistedException;
import com.everett.models.Message;
import com.everett.models.User;
import com.everett.services.PostService;
import com.everett.services.UserService;

@Stateless(name = "UserAPI")
@Path("/users")
public class UserAPI {
    @Context
    SecurityContext securityContext;

    @Inject
    UserService userService;

    @Inject
    PostService postService;

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings({ "rawtypes" })
    public Response addUserFromContext() {
        KeycloakPrincipal principal = (KeycloakPrincipal) securityContext.getUserPrincipal();
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();

        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        User user = new User(accessToken.getPreferredUsername(), accessToken.getGivenName(),
                accessToken.getFamilyName(), accessToken.getEmail(), createdTime, "active");

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        return Response.ok(userService.getAllUsers()).build();
    }

    @Path("/whoami")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserFromContext() {
        return Response.ok(userService.getUserFromContext(securityContext)).build();
    }

    @Path("/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByEmail(@PathParam("email") String email) {
        try {
            return Response.ok(userService.getUserByEmail(email)).build();
        } catch (UserNotFoundException e) {
            Message message = new Message("User with email " + email + " not found");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
    }

    @Path("/posts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUserPost() {
        return Response.ok(postService.getAllUserPosts(securityContext)).build();
    }
}
