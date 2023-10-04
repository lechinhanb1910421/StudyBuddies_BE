package com.everett.apis;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Message;
import com.everett.services.UserNotificationService;

@Path("/userNotifications")
@RequestScoped
@DenyAll
public class UserNotificationAPI {
    // private static final Logger logger = LogManager.getLogger(UserNotificationAPI.class);

    @Inject
    UserNotificationService userNotificationService;

    @Path("/userId/{userId}")
    @GET
    @RolesAllowed({ "admin", "visitor" })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserNotificationsById(@PathParam("userId") Long userId) {
        try {
            return Response.ok(userNotificationService.getUserNotification(userId)).build();
        } catch (UserNotFoundException e) {
            Message message = new Message("User with id " + userId + " not found");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
    }
}
