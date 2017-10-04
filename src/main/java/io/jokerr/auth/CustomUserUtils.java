package io.jokerr.auth;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author jokerr
 */
public final class CustomUserUtils {

    public static CustomUser getUser() {
        return (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
