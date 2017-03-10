package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;

@Controller
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserRepositoryService userRepositoryService;

    @RequestMapping(value = "/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        LOG.info("Current session id {} ", RequestContextHolder.currentRequestAttributes().getSessionId());
        return "login";
    }

    @RequestMapping(value = "/api_login")
    public ResponseEntity<String> validateApiLogin(@RequestParam("username") String userName,
                                         @RequestParam("password") String password) {
        if (isUserPresent(userName)) {
            User user = userRepositoryService.getUserByUserName(userName).get();
            if (isPasswordCorrect(user, password)) {
                return new ResponseEntity<>(user.getApiKey(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Hibás belépési adatok.", HttpStatus.BAD_REQUEST);
    }

    private boolean isUserPresent(String userName) {
        return userRepositoryService.getUserByUserName(userName).isPresent();
    }

    private boolean isPasswordCorrect(User user, String password) {
        return User.PASSWORD_ENCODER.matches(password, user.getPasswordHash());
    }
}
