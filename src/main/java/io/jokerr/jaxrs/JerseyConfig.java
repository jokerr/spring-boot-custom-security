package io.jokerr.jaxrs;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

/**
 * @author jokerr
 */
@Configuration
@ApplicationPath("/v1")
public class JerseyConfig extends ResourceConfig{

    public JerseyConfig() {
        packages(JerseyConfig.class.getPackage().getName());
    }
}
