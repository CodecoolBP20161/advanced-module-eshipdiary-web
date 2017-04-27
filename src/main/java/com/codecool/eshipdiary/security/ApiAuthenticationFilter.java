package com.codecool.eshipdiary.security;

import com.codecool.eshipdiary.service.ApiAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiAuthenticationFilter extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(ApiAuthenticationFilter.class);

    @Autowired
    ApiAuthenticationService apiAuthenticationService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        Authentication authentication;

        try {
            authentication = apiAuthenticationService.getAuth(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOG.debug("REST authentication successful with user: {}",
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (InsufficientAuthenticationException invalidToken) {
            LOG.debug("REST auth failed: {}", invalidToken.getMessage());
            httpServletResponse.sendError(401, invalidToken.getMessage());
        } catch (AuthenticationCredentialsNotFoundException noToken) {
            LOG.debug("{}, moving on to next authentication link", noToken.getMessage());
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
