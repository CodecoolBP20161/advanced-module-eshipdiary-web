package com.codecool.eshipdiary.controller;


import com.codecool.eshipdiary.model.APIKeyValidator;
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

    @RequestMapping(value = "/check_api_key", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public APIKeyValidator validateAPIKey(@RequestParam("apikey") String apiKey) {
        if(userRepositoryService.getUserByAPIKey(apiKey).isPresent()) {
            return new APIKeyValidator(true);
        }

        return new APIKeyValidator(false);
    }
}
