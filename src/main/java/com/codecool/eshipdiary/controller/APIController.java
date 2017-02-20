package com.codecool.eshipdiary.controller;


import com.codecool.eshipdiary.model.AppValidator;
import com.codecool.eshipdiary.model.RemoteLoginResponse;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class APIController {

    @Autowired
    UserRepositoryService userRepositoryService;

    @RequestMapping(value = "/validate-app", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public AppValidator validateAPIKey(@RequestParam("key") String apiKey) {
        Optional<User> user = userRepositoryService.getUserByAPIKey(apiKey);
        if(user.isPresent() && user.get().isActive()) {
            return new AppValidator(true);
        }
        return new AppValidator(false);
    }

    @RequestMapping(value = "/remote-auth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public RemoteLoginResponse remoteAuth(@RequestParam("username") String username,
                                          @RequestParam("password") String password) {
        RemoteLoginResponse remoteLoginResponse = new RemoteLoginResponse();
        Optional<User> userOptional = userRepositoryService.getUserByUserName(username);
        if (userOptional.isPresent() && userOptional.get().isActive()) {
            User user = userOptional.get();
            if (User.PASSWORD_ENCODER.matches(password, user.getPasswordHash())) {
                remoteLoginResponse = new RemoteLoginResponse(user);
            }
        }
        return remoteLoginResponse;
    }
}
