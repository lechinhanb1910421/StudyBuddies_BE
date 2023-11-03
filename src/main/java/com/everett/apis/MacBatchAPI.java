package com.everett.apis;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

    @Path("/account")
    @POST
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response testSubmitThread(@MultipartForm MultipartFormDataInput formInput) {
        try {
            String stackId = macService.importUserAccount(formInput, triggerUserId, triggerUserEmail);
            return Response.status(201).entity(stackId).build();
        } catch (BusinessException e) {
            Message errorMessage = new Message(e.getMessage());
            return Response.status(500).entity(errorMessage).build();
        }
    }

}
