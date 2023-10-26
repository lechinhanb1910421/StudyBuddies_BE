package com.everett.apis;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.jwt.Claim;

import com.everett.dtos.TokenReceiveDTO;
import com.everett.exceptions.checkedExceptions.DeviceNotFoundException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Message;
import com.everett.services.DeviceService;

@Path("/notifications")
public class NotificationAPI {
    private static final Logger logger = LogManager.getLogger(NotificationAPI.class);

    @Inject
    @Claim("email")
    private String email;

    @Inject
    DeviceService deviceService;

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewSubscription(TokenReceiveDTO payload) {
        logger.info(
                "CREATING NEW DEVICE FOR USER " + email + " WITH TOKEN " + payload.getToken().substring(0, 27) + "...");
        try {
            deviceService.addDevice(email, payload.getToken());
            Message message = new Message("Device added successfully");
            return Response.ok(message).build();
        } catch (UserNotFoundException e) {
            logger.info("Can not find user with email " + email);
            Message message = new Message("Can not find user with email " + email);
            throw new WebApplicationException(Response.status(400).entity(message).build());
        } catch (Exception e) {
            logger.error("Internal Server Error");
            Message message = new Message("Internal Server Error");
            throw new WebApplicationException(Response.status(500).entity(message).build());
        }
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response getAllDevices() {
        logger.info("GET ALL DEVICES");
        try {
            return Response.ok(deviceService.getAllDevices()).build();
        } catch (Exception e) {
            logger.error("Internal Server Error");
            Message message = new Message("Internal Server Error");
            throw new WebApplicationException(Response.status(500).entity(message).build());
        }
    }

    @Path("/devices/{deviceId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response deleteDeviceById(@PathParam("deviceId") Long deviceId) {
        logger.info("DELETE DEVICE WITH ID: " + deviceId);
        try {
            deviceService.deleteDeviceById(deviceId);
            Message message = new Message("Device with ID: " + deviceId + " was deleted successfully");
            return Response.ok(message).build();
        } catch (DeviceNotFoundException e) {
            logger.error("DEVICE WITH ID: " + deviceId + " NOT FOUND");
            Message message = new Message("device with id: " + deviceId + " not found");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        } catch (Exception e) {
            logger.error("INTERNAL SERVER ERROR");
            Message message = new Message("Internal Server Error");
            throw new WebApplicationException(Response.status(500).entity(message).build());
        }
    }
}
