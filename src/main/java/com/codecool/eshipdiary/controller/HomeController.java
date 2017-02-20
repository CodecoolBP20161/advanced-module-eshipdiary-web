package com.codecool.eshipdiary.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().toString().contains("ADMIN")){
            return "redirect:/users";
        }
        return "index";
    }

    @RequestMapping(value = "/users")
    public String getUserTable() {
        return "users";
    }

}
