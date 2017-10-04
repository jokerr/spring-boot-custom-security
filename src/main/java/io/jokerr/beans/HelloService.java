package io.jokerr.beans;

import io.jokerr.auth.Roles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;

/**
 * @author jokerr
 */
@Component
public class HelloService {

    @RolesAllowed(Roles.ROLE_USER)
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String getUserHelloWorld() {
        return "User message";
    }

//    @RolesAllowed(Roles.ROLE_ADMIN)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String getAdminHelloWorld() {
        return "All your bases are belong to us";
    }
}
