package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.ApiAuthentication;
import com.codecool.eshipdiary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by hamargyuri on 2017. 03. 02..
 */
@Service
public class ApiAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    @Autowired
    UserRepositoryService userRepositoryService;

    public Authentication getAuth(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        Optional<User> user = userRepositoryService.getUserByApiToken(token);
        if (token == null) {
            throw new AuthenticationCredentialsNotFoundException("No token.");
        }
        if (user.isPresent()) {
            return new ApiAuthentication(user.get());
        }
        throw new InsufficientAuthenticationException("invalid token");
    }
}
