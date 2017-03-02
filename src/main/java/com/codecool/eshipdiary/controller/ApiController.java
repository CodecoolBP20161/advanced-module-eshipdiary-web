package com.codecool.eshipdiary.controller;


import com.codecool.eshipdiary.model.AppValidator;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApiController {

    @Autowired
    UserRepositoryService userRepositoryService;

    private boolean isUserPresent(String userName) {
        return userRepositoryService.getUserByUserName(userName).isPresent();
    }

    private boolean isPasswordCorrect(User user, String password) {
        return User.PASSWORD_ENCODER.matches(password, user.getPasswordHash());
    }

    @RequestMapping(value = "/api_login", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public AppValidator validateAPIKey(@RequestParam("username") String userName,
                                         @RequestParam("password") String password) {
        AppValidator appValidator = new AppValidator(null);
        if (isUserPresent(userName)) {
            User user = userRepositoryService.getUserByUserName(userName).get();
            if (isPasswordCorrect(user, password)) {
                appValidator = new AppValidator(user.getApiKey());
            }
        }
        return appValidator;
    }

}
