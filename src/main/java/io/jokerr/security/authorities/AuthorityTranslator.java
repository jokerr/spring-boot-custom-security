package io.jokerr.security.authorities;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface AuthorityTranslator {

    List<GrantedAuthority> getAuthorities(String... roles);
}
