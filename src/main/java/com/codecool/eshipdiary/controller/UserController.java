package com.codecool.eshipdiary.controller;


import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;


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

    @RequestMapping(value = "users/new", method = RequestMethod.GET)
    public String userForm() {
        return "user_form";
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if(result.hasErrors()) {
            LOG.info("result" + result.getFieldErrors());
            return "user_form";
        }
        return "users";
    }
    
}
