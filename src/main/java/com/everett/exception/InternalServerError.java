
package com.everett.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.everett.model.Message;

public class InternalServerError extends WebApplicationException {
    public InternalServerError(String err) {
        super(Response.status(400).entity(new Message("Internal Server Error!!!. " + err)).build());
    }
}
