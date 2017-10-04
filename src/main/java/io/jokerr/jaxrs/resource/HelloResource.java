package io.jokerr.jaxrs.resource;

import io.jokerr.auth.CustomUserUtils;
import io.jokerr.auth.Roles;
import io.jokerr.beans.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author jokerr
 */
@Produces(MediaType.TEXT_PLAIN)
@Path("/hello")
public class HelloResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloResource.class);

    @Inject
    private HelloService service;

    @GET
    @RolesAllowed({Roles.ROLE_ADMIN, Roles.ROLE_USER})
    public Response getUserHelloWorld() {
        LOGGER.info("User " + CustomUserUtils.getUser().getUsername() + " entered getUserHelloWorld");
        return Response.ok(service.getUserHelloWorld()).build();
    }

    @GET
    @Path("/admin")
    @RolesAllowed(Roles.ROLE_ADMIN)
    public Response getAdminHelloWorld() {
        LOGGER.info("User " + CustomUserUtils.getUser().getUsername() + " entered getAdminHelloWorld");
        return Response.ok(service.getAdminHelloWorld()).build();
    }
}
