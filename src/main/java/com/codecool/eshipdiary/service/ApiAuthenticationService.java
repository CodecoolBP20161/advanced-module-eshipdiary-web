package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.model.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by hamargyuri on 2017. 03. 02..
 */
public class ApiAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    @Autowired
    UserRepositoryService userRepositoryService;

    private boolean isApiKeyValid(String apiKey) {
        Optional<User> user = userRepositoryService.getUserByAPIKey(apiKey);
        return user.isPresent();
    }

    public Authentication getAuth(HttpServletRequest request) {
        final String apiKey = request.getHeader(AUTH_HEADER_NAME);
        if (isApiKeyValid(apiKey)) {
            final User user = userRepositoryService.getUserByAPIKey(apiKey).get();
            return new UserAuthentication(user);
        }
        return null;
    }
}
