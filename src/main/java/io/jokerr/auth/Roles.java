package io.jokerr.auth;

/**
 * @author jokerr
 */
public final class Roles {
    // In Spring Security the roles must begin with "ROLE_" otherwise the JSR250, Secured, or prePost Authorization
    // annotations will not work...

    public static final String ROLE_USER = "ROLE_USER";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_DEFAULT = "default";    //this role will not work!
}
