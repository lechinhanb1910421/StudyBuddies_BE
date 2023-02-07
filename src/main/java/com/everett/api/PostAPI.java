package com.everett.api;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.everett.exception.InvalidSearchKeywordException;
import com.everett.service.PostServices;

@Path("/posts")
public class PostAPI {
    private static final Logger logger = LogManager.getLogger(PostAPI.class);

    @Inject
    PostServices postService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts() {
        logger.info("Get All Posts");
        return Response.ok(postService.getAllPosts()).build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response seachRequestsByReason(@QueryParam("keywords") String keywords) {
        if (keywords == null) {
            throw new InvalidSearchKeywordException("Keyword must not be null");
        }
        return Response.ok(postService.seachRequestsByKeywords(keywords)).build();
    }
}
