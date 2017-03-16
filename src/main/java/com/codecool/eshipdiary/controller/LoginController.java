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

import java.util.HashMap;

@Controller
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserRepositoryService userRepositoryService;

    @RequestMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        LOG.info("Current session id {} ", RequestContextHolder.currentRequestAttributes().getSessionId());
        return "login";
    }

    @RequestMapping(value = "/api_login", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> validateApiLogin(@RequestParam("username") String userName,
                                                                @RequestParam("password") String password) {
        HashMap<String, String> data = new HashMap<>();
        if (userRepositoryService.getUserByUserName(userName).isPresent()) {
            User user = userRepositoryService.getUserByUserName(userName).get();
            if (User.PASSWORD_ENCODER.matches(password, user.getPasswordHash())) {
                if (user.isActive()) {
                    LOG.info("New login via api: {}", user.getUserName());
                    data.put("token", user.getApiToken());
                    data.put("id", user.getId().toString());
                    return new ResponseEntity<>(data, HttpStatus.OK);
                }
                LOG.debug("Inactive user tried to log in via api: {}", user.getUserName());
                data.put("message", "Inaktív felhasználó.");
                return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
            }
        }
        data.put("message", "Hibás belépési adatok.");
        return new ResponseEntity<>(data, HttpStatus.UNAUTHORIZED);
    }
}