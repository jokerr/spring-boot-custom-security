package io.jokerr.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author jokerr
 */
public class CustomFilter extends GenericFilterBean {

    private AuthenticationManager authenticationManager;

    public CustomFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
    throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Optional<String> username = Optional.ofNullable(request.getHeader("X-Username"));
        Optional<String> role = Optional.ofNullable(request.getHeader("X-role"));
        if(!username.isPresent()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("No username bro");
            return;
        }

        CustomUser user = new CustomUser();
        user.setUsername(username.get());

        Authentication authentication = attemptAuthentication(new CustomAuthentication(user, getAuthorities(role)));

        logger.info("Authenticated " + user.getUsername() + " authorities: " + authentication.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    List<GrantedAuthority> getAuthorities(Optional<String> role) {
        if(!role.isPresent()) {
            return AuthorityUtils.createAuthorityList(Roles.ROLE_DEFAULT);
        }
        if(Roles.ROLE_ADMIN.equalsIgnoreCase(role.get())) {
            return AuthorityUtils.createAuthorityList(Roles.ROLE_ADMIN);
        }
        else {
            return AuthorityUtils.createAuthorityList(Roles.ROLE_USER);
        }
    }

    private Authentication attemptAuthentication(Authentication requestAuthentication) {
        Authentication authentication = authenticationManager.authenticate(requestAuthentication);
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate user.");
        }
        return authentication;
    }
}
