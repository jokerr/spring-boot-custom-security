package io.jokerr.security;

import io.jokerr.security.authorities.AuthorityTranslator;
import io.jokerr.security.authorities.DefaultAuthorityTranslator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author jokerr
 */
public class CustomFilter extends GenericFilterBean {

    private AuthenticationManager authenticationManager;
    private AuthorityTranslator authorityTranslator;
    private AuthenticationFailureHandler authenticationFailureHandler = new CustomAuthenticationFailureHandler();
    private AuthenticationSuccessHandler authenticationSuccessHandler = new CustomAuthenticationSuccessHandler();

    public CustomFilter(AuthenticationManager authenticationManager) {
        this(authenticationManager, new DefaultAuthorityTranslator());
    }

    public CustomFilter(AuthenticationManager authenticationManager, AuthorityTranslator authorityTranslator) {
        this.authenticationManager = authenticationManager;
        this.authorityTranslator = authorityTranslator;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
    throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Optional<String> username = Optional.ofNullable(request.getHeader("X-Username"));
        Optional<String> role = Optional.ofNullable(request.getHeader("X-role"));

        CustomUser user = new CustomUser();
        user.setUsername(username.orElse("").trim());

        Authentication authentication;
        try {
            authentication = attemptAuthentication(new CustomAuthentication(
                    user,
                    authorityTranslator.getAuthorities(role.orElse(""))));
        }
        catch(AuthenticationException e) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            return;
        }

        if(authenticationSuccessHandler != null) {
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private Authentication attemptAuthentication(Authentication requestAuthentication) {
        Authentication authentication = authenticationManager.authenticate(requestAuthentication);
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate user.");
        }
        return authentication;
    }
}
