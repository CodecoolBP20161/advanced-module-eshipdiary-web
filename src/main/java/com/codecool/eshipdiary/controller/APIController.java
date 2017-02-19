package com.codecool.eshipdiary.controller;


import com.codecool.eshipdiary.model.APIKeyValidator;
import com.codecool.eshipdiary.model.RemoteLoginResponse;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class APIController {

    @Autowired
    UserRepositoryService userRepositoryService;

    @RequestMapping(value = "/validate_api_key", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public APIKeyValidator validateAPIKey(@RequestParam("key") String apiKey) {
        if(userRepositoryService.getUserByAPIKey(apiKey).isPresent()) {
            return new APIKeyValidator(true);
        }
        return new APIKeyValidator(false);
    }

    @RequestMapping(value = "/remote_auth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public RemoteLoginResponse remoteAuth(@RequestParam("username") String username,
                                          @RequestParam("password") String password) {
        RemoteLoginResponse remoteLoginResponse = new RemoteLoginResponse();
        if (userRepositoryService.getUserByUserName(username).isPresent()) {
            User user = userRepositoryService.getUserByUserName(username).get();
            if (User.PASSWORD_ENCODER.matches(password, user.getPasswordHash())) {
                remoteLoginResponse = new RemoteLoginResponse(user);
            }
        }
        return remoteLoginResponse;
    }
}
