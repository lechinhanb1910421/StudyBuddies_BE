package com.everett.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.everett.models.Message;

public class IdNotFoundException extends WebApplicationException {
    public IdNotFoundException(Long id) {
        super(Response.status(400).entity(new Message("Post with id = " + id + " does not exist")).build());
    }
}
