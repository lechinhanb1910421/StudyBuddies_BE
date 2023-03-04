package com.everett.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.everett.models.Message;

public class UserNotFoundWebException extends WebApplicationException {
    public UserNotFoundWebException(Long id) {
        super(Response.status(400).entity(new Message("User with id = " + id + " does not exist")).build());
    }
}
