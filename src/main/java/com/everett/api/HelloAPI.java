package com.everett.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloAPI {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response sayHello() {
        String out = "{\"status\":\"OK\", \"message\":\"Hello from the server side\"}";
        return Response.ok(out).build();
    }
}
