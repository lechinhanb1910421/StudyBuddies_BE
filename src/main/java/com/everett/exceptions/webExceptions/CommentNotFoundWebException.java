package com.everett.exceptions.webExceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.everett.models.Message;

public class CommentNotFoundWebException extends WebApplicationException {
    public CommentNotFoundWebException(Long id) {
        super(Response.status(400).entity(new Message("Comment with id = " + id + " does not exist")).build());
    }
}
