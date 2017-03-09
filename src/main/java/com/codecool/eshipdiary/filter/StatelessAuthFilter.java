package com.codecool.eshipdiary.filter;

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
import java.io.IOException;

/**
 * Created by hamargyuri on 2017. 02. 28..
 */
@Component
public class StatelessAuthFilter extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(StatelessAuthFilter.class);

    @Autowired
    ApiAuthenticationService apiAuthenticationService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Authentication authentication;

        try {
            authentication = apiAuthenticationService.getAuth(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOG.info("api authentication successful: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        } catch (InsufficientAuthenticationException invalidToken) {
            LOG.info("api auth failed {}", invalidToken.getMessage());
        } catch (AuthenticationCredentialsNotFoundException noToken) {
            LOG.debug("{}, moving on to next authentication link", noToken.getMessage());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
