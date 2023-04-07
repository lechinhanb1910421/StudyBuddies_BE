package com.everett.apis;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Message;
import com.everett.services.AdminService;
import com.everett.services.UserService;

@ApplicationScoped
@Path("/admin")
@DenyAll
public class AdminAPI {
    private static final Logger logger = LogManager.getLogger(AdminAPI.class);

    @Inject
    UserService userService;

    @Inject
    AdminService adminService;

    @Path("/users")
    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        logger.info("GET ALL USER FROM ADMIN");
        return Response.ok(userService.getAllUsers()).build();
    }

    @Path("/users/{userId}/posts/count")
    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPostsCount(@PathParam("userId") Long userId) {
        logger.info("GET NUMBER OF POSTS FROM USER");
        try {
            Integer postsCount = adminService.getUserPostsCount(userId);
            return Response.ok(postsCount).build();
        } catch (UserNotFoundException e) {
            Message message = new Message("User with id " + userId + " not found");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
    }

    @Path("/")
    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBriefStats() {
        logger.info("GET ALL STATS FROM ADMIN");
        return Response.ok(adminService.getBriefStats()).build();
    }

}
