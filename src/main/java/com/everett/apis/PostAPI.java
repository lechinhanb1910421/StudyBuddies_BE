package com.everett.apis;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.dtos.PostReceiveDTO;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.exceptions.checkedExceptions.EmptyReactionException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.exceptions.webExceptions.InvalidSearchKeywordException;
import com.everett.models.Message;
import com.everett.services.PostService;

@Path("/posts")
public class PostAPI {
    private static final Logger logger = LogManager.getLogger(PostAPI.class);

    @Inject
    PostService postService;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts() {
        logger.info("GET ALL POSTS");
        return Response.ok(postService.getAllPosts()).build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response seachRequestsByReason(@QueryParam("keywords") String keywords) {
        if (keywords == null) {
            throw new InvalidSearchKeywordException("Keyword must not be null");
        }
        logger.info("SEARCH POSTS BY KEYWORD: " + keywords);
        return Response.ok(postService.seachPostsByKeywords(keywords)).build();
    }

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPost(PostReceiveDTO payload) {
        if (payload.isMissingKeys()) {
            logger.error("MISSING KEYS IN REQUEST BODY");
            Message errMsg = new Message("Missing key(s) in request body");
            throw new WebApplicationException(Response.status(400).entity(errMsg).build());
        }
        try {
            postService.createPost(payload, securityContext);
        } catch (UserNotFoundException e) {
            Message message = new Message("User not found");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
        logger.info("NEW POST WAS CREATED SUCCESSFULLY FROM");
        Message message = new Message("New Post was created successfully");
        // isUpdated.set(true);
        return Response.ok(message).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostById(@PathParam("id") Long id) {
        logger.info("GET POST BY ID: " + id);
        return Response.ok(postService.getPostResponseById(id)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePost(@PathParam("id") Long id, PostReceiveDTO payload) {
        if (payload.isMissingKeys()) {
            logger.error("UPDATE POST REQUEST IS MISSING KEYS");
            Message message = new Message("Update post request is missing keys");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
        postService.updatePost(id, payload);
        logger.info("Post with id: " + id + " was updated successfully");
        logger.info("POST WITH ID: " + id + " WAS UPDATED SUCCESSFULLY");
        Message message = new Message("Post was updated successfully");
        // isUpdated.set(true);
        return Response.ok(message).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePost(@PathParam("id") Long id) {
        postService.deletePost(id);
        logger.info("POST " + id + " WAS DELETED SUCCESSFULLY");
        System.out.println("Post " + id + " was deleted successfully");
        Message message = new Message("Post was deleted successfully");
        // isUpdated.set(true);
        return Response.ok(message).build();
    }

    @Path("/{id}/react")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response reactPost(@PathParam("id") Long id) {
        Message message = new Message("Post was reacted successfully");
        postService.reactPost(id, securityContext);
        return Response.ok(message).build();
    }

    @Path("/{id}/react")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeReactPost(@PathParam("id") Long id) {
        try {
            postService.removeReactPost(id, securityContext);
            return Response.ok().build();
        } catch (Exception e) {
            Message message = new Message("Can not unset reaction");
            return Response.ok(message).build();
        }
    }

    @Path("/{id}/react")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPostReation(@PathParam("id") Long id) {
        try {
            return Response.ok(postService.getAllPostReation(id)).build();
        } catch (EmptyReactionException e) {
            Message message = new Message("This post has no reaction yet");
            return Response.ok(message).build();
        } catch (EmptyEntityException e2) {
            Message message = new Message("Post with id: " + id + " not found");
            throw new WebApplicationException(Response.status(400).entity(message).build());
        }
    }

}
