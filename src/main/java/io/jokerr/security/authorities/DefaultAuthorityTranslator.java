package io.jokerr.security.authorities;

import io.jokerr.security.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class DefaultAuthorityTranslator implements AuthorityTranslator {
    @Override
    public List<GrantedAuthority> getAuthorities(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(0);
        for(String role : roles) {
            if(Roles.ROLE_ADMIN.equalsIgnoreCase(role)) {
                authorities.add(new SimpleGrantedAuthority(Roles.ROLE_ADMIN));
            }
            else {
                authorities.add(new SimpleGrantedAuthority(Roles.ROLE_USER));
            }
        }

        if(authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority(Roles.ROLE_DEFAULT));
        }

        return authorities;
    }
}
