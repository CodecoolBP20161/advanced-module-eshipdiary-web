package com.codecool.eshipdiary.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;

@Controller
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/login")
    public String login() {
        LOG.info("Current session id {} ", RequestContextHolder.currentRequestAttributes().getSessionId());
        return "login";
    }
}
