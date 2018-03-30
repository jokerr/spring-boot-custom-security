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
import org.springframework.util.Assert;
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
    private AuthorityTranslator authorityTranslator = new DefaultAuthorityTranslator();
    private AuthenticationFailureHandler authenticationFailureHandler = new CustomAuthenticationFailureHandler();
    private AuthenticationSuccessHandler authenticationSuccessHandler = new CustomAuthenticationSuccessHandler();

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

        CustomUser user = new CustomUser();
        user.setUsername(username.orElse("").trim());

        Authentication authentication;
        try {
            authentication = attemptAuthentication(new CustomAuthentication(
                    user, authorityTranslator.getAuthorities(role.orElse(""))));
        }
        catch (InternalAuthenticationServiceException e) {
            logger.error("An internal error occurred while trying to authenticate the user.", e);
            unsuccessfulAuthentication(request, response, e);
            return;
        }
        catch(AuthenticationException e) {
            unsuccessfulAuthentication(request, response, e);
            return;
        }

        if(authenticationSuccessHandler != null) {
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    public AuthorityTranslator getAuthorityTranslator() {
        return authorityTranslator;
    }

    public void setAuthorityTranslator(AuthorityTranslator authorityTranslator) {
        this.authorityTranslator = authorityTranslator;
    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        Assert.notNull(authenticationFailureHandler, "failureHandler cannot be null");
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return authenticationSuccessHandler;
    }

    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        Assert.notNull(authenticationSuccessHandler, "successHandler cannot be null");
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    private Authentication attemptAuthentication(Authentication requestAuthentication) {
        // Do any additional processing here if needed.  In this example we just need to check the auth
        return authenticationManager.authenticate(requestAuthentication);
    }

    /**
     * Default behaviour for unsuccessful authentication.  This method will clear the {@link SecurityContextHolder}
     * and delegates additional behaviour to the {@link AuthenticationFailureHandler}.
     */
    private void unsuccessfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException exception)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + exception.toString(), exception);
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
            logger.debug("Delegating to authentication failure handler " + authenticationFailureHandler);
        }

        authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    }
}
