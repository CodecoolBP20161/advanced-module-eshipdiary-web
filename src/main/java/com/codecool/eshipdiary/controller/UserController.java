package com.codecool.eshipdiary.controller;


import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepositoryService userRepositoryService;

    @ModelAttribute("user")
    public User User() {
        return new User();
    }

    @RequestMapping("/users")
    public String getUserTable(Model model) {
        model.addAttribute("user");
        return "users";
    }
    
}
