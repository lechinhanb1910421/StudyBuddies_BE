package com.everett.apis;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.models.Message;
import com.everett.services.PictureService;

@ApplicationScoped
@Path("/pictures")
public class PictureAPI {
    private static final Logger logger = LogManager.getLogger(UserAPI.class);

    @Inject
    PictureService pictureService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPictures() {
        logger.info("GET ALL PICTURES");
        return Response.ok(pictureService.getAllPictures()).build();
    }

    @GET
    @Path("/{picId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPictureById(@PathParam("picId") Long picId) {
        try {
            logger.info("GET PICTURE ID: " + picId);
            return Response.ok(pictureService.getPictureById(picId)).build();
        } catch (EmptyEntityException e) {
            Message message = new Message("Can not find picture with id: " + picId);
            logger.error("PICTURE WITH ID " + picId + " DOES NOT EXIST");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
    }

    @POST
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPicToPost(@PathParam("postId") Long postId, @QueryParam("picUrl") String picUrl) {
        try {
            pictureService.addPostPicture(postId, picUrl);
            logger.info("SET PICTURE URL" + picUrl + "TO POST: " + postId);
            Message message = new Message("Picture was successfully added to post with id: " + postId);
            return Response.ok(message).build();
        } catch (EmptyEntityException e) {
            Message message = new Message("Can not find post with id: " + postId);
            logger.error("POST WITH ID " + postId + " DOES NOT EXIST");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
    }
}
