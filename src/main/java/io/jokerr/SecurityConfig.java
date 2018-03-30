package io.jokerr;

import io.jokerr.security.CustomAuthenticationFailureHandler;
import io.jokerr.security.CustomAuthenticationProvider;
import io.jokerr.security.CustomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author jokerr
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {

        // The CustomFilter + CustomAuthenticationProvider makes this somewhat obsolete but it's good to overwrite
        // this method anyways.  In this case we're just sending the user a 401.

        // Could send a redirect in the real world
        // response.sendRedirect( SOME URL HERE ));

        return (request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No authentication found");
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomFilter filter = new CustomFilter(authenticationManager());
        filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .anonymous().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint())
                .and()
                .addFilterBefore(filter, BasicAuthenticationFilter.class);
    }

    @Bean
    public static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // In Spring Security the roles must begin with "ROLE_" otherwise the JSR250, Secured, or prePost
        // authorization annotations will not work.  This bean will will let us define the roles how we want to.
        return new GrantedAuthorityDefaults("");
    }
}
