package com.codecool.eshipdiary.filter;

import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hamargyuri on 2017. 03. 10..
 */
@Component
public class IsActiveFilter extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(ApiAuthenticationFilter.class);

    @Autowired
    UserRepositoryService userRepositoryService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        try {
            User user = userRepositoryService.getUserByUserName(httpServletRequest.getUserPrincipal().getName()).get();
            if (!user.isActive()) {
                httpServletResponse.sendError(403, "inactive user");
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }

        } catch (NullPointerException n) {

            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
