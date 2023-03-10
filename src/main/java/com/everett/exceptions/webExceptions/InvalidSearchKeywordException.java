package com.everett.exceptions.webExceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.everett.models.Message;

public class InvalidSearchKeywordException extends WebApplicationException {
    public InvalidSearchKeywordException(String err) {
        super(Response.status(400).entity(new Message(err)).build());
    }
}
