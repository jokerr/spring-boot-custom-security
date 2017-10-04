package io.jokerr.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author jokerr
 */
public class CustomAuthentication implements Authentication {
    private final Object principal;
    private List<GrantedAuthority> authorities;
    private boolean authenticated = false;
    private Object details = null;

    public CustomAuthentication(Object principal, List<GrantedAuthority> authorities) {
        if(authorities == null || authorities.stream().anyMatch(a -> a == null)) {
            throw new IllegalArgumentException("Authorities cannot be null or contain any null elements");
        }
        this.authorities = Collections.unmodifiableList(authorities);
        this.principal = principal;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        if(this.principal instanceof CustomUser) {
            return ((CustomUser)principal).getUsername();
        }
        return "";
    }
}
