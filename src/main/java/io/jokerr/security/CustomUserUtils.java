package io.jokerr.security;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author jokerr
 */
public final class CustomUserUtils {

    private CustomUserUtils() {
        //no-op
    }

    public static CustomUser getUser() {
        return (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
