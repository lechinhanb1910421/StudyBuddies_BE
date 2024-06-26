package com.everett.apis;

import java.io.IOException;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.jwt.Claim;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.everett.exceptions.checkedExceptions.BusinessException;
import com.everett.models.Message;
import com.everett.services.MacService;

@Path("/mac-job")
@RequestScoped
@DenyAll
public class MacBatchAPI {
    private static final Logger logger = LogManager.getLogger(MacBatchAPI.class);

    @Inject
    private MacService macService;

    @Inject
    @Claim("preferred_username")
    private String triggerUserId;

    @Inject
    @Claim("email")
    private String triggerUserEmail;

    @POST
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createMacJob(@MultipartForm MultipartFormDataInput formInput) {
        try {
            logger.info("CREATING MAC JOB FOR USER: [" + triggerUserEmail + "]");
            String stackId = macService.importUserAccount(formInput, triggerUserId, triggerUserEmail);
            return Response.status(201).entity(stackId).build();
        } catch (BusinessException e) {
            Message errorMessage = new Message(e.getMessage());
            return Response.status(500).entity(errorMessage).build();
        }
    }

    @Path("/{stackId}")
    @GET
    @RolesAllowed("ADMIN")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON })
    public Response getMacJobReportByStackId(@PathParam("stackId") String stackId) {
        try {
            logger.info("EXPORTING MAC JOB REPORT FOR STACK ID: [" + stackId + "]");
            String fileName = macService.buildMacReportName(stackId);
            return Response.ok(macService.exportMacResultFile(stackId))
                    .header("Content-Disposition", "attachment; filename=" + fileName).build();
        } catch (IOException e) {
            logger.info("AN ERROR OCCURRED WHEN EXPORTING REPORT FOR STACK ID " + stackId);
            Message mess = new Message("An error occurred when exporting report for stack id " + stackId);
            return Response.status(500).type(MediaType.APPLICATION_JSON).entity(mess).build();
        } catch (BusinessException e) {
            logger.info("CANNOT FIND STACK ID " + stackId);
            Message mess = new Message("Cannot find stack id " + stackId);
            return Response.status(404).type(MediaType.APPLICATION_JSON).entity(mess).build();
        }
    }

}
