package com.everett.apis;

import javax.annotation.security.DenyAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

import com.everett.services.UserNotificationService;

@Path("/userNotifications")
@RequestScoped
@DenyAll
public class UserNotificationAPI {
    // private static final Logger logger = LogManager.getLogger(UserNotificationAPI.class);

    @Inject
    UserNotificationService userNotificationService;

    // @Path("/userId/{userId}")
    // @GET
    // @RolesAllowed({ "admin", "visitor" })
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response getUserNotificationsById(@PathParam("userId") Long userId) {
    //     try {
    //         return Response.ok(userNotificationService.getUserNotificationByEmail(userId)).build();
    //     } catch (UserNotFoundException e) {
    //         Message message = new Message("User with id " + userId + " not found");
    //         throw new WebApplicationException(Response.status(400).entity(message).build());
    //     }
    // }
}
