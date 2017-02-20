package com.codecool.eshipdiary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/users")
    public String getUserTable() {
        return "users";
    }

}
