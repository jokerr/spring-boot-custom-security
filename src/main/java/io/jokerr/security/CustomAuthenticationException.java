package io.jokerr.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @author jokerr
 */
public class CustomAuthenticationException extends AuthenticationException {
    public CustomAuthenticationException(String msg) {
        super(msg);
    }
}
