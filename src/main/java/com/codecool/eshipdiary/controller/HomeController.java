package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserRepositoryService userRepositoryService;

    @RequestMapping("/")
    public String index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().toString().contains("ADMIN")){
            return "redirect:/users";
        }
        return "index";
    }
}
