package com.codecool.eshipdiary.filter;

import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.security.Principal;

@Component
public class IsActiveFilter extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(ApiAuthenticationFilter.class);

    @Autowired
    UserRepositoryService userRepositoryService;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        Principal principal = httpServletRequest.getUserPrincipal();
        if (principal == null ||
                userRepositoryService.getUserByUserName(principal.getName()).map(User::isActive).orElse(false)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
            httpServletResponse.sendError(403, "inactive user");
        }
    }
}
