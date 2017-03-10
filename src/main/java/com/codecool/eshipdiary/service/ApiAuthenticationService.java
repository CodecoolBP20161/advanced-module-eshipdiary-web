package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.model.ApiAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
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
        final String apiKey = request.getHeader(AUTH_HEADER_NAME);
        Optional<User> user = userRepositoryService.getUserByAPIKey(apiKey);
        if (apiKey == null) {
            throw new AuthenticationCredentialsNotFoundException("No token.");
        }
        if (user.isPresent()) {
            if (user.get().isActive()) {
                return new ApiAuthentication(user.get());
            }
            throw new DisabledException("inactive user");
        }
        throw new InsufficientAuthenticationException("invalid token");
    }
}
