package com.everett.apis;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.models.Message;
import com.everett.services.CommentService;

@Path("/comments")
// @Stateless(name = "CommentAPI")
public class CommentAPI {
    private static final Logger logger = LogManager.getLogger(PostAPI.class);

    @Context
    SecurityContext securityContext;

    @Inject
    CommentService commentService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllComments() {
        try {
            return Response.ok(commentService.getAllComments()).build();
        } catch (Exception e) {
            Message message = new Message("Something was wrong!");
            e.printStackTrace();
            return Response.status(500).entity(message).build();
        }
    }

    @Path("/{postId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("postId") Long postId, @QueryParam("content") String content) {
        try {
            commentService.addComment(postId, content, securityContext);
            logger.info("Comment" + postId + ", content: " + content + " was successfully added");
            System.out.println("Comment" + postId + ", content: " + content + " was successfully added");
            Message message = new Message("Comment was successfully added");
            return Response.ok(message).build();
        } catch (Exception e) {
            Message message = new Message("Something was wrong!");
            e.printStackTrace();
            return Response.status(500).entity(message).build();
        }
    }
}
