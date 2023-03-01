package com.everett.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.everett.models.Message;

public class MajorNotFoundWebException extends WebApplicationException {
    public MajorNotFoundWebException(Long id) {
        super(Response.status(400).entity(new Message("Major with id = " + id + " does not exist")).build());
    }
}
