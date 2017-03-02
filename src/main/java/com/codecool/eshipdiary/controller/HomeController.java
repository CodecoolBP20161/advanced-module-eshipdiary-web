package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

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


    @RequestMapping(value = "/users")
    public String getUserTable(Model model) {
        return "users";
    }

    @RequestMapping(value = "/users" , method = RequestMethod.POST)
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // todo: frontend input error handling
            LOG.error("Error while trying to create a new user", bindingResult);
            return "users";
        } else {
            LOG.info("User: ", user);
            userRepositoryService.save(user);
            return "users";
        }
    }

    @RequestMapping(value = "/users/{usersId}")
    public String updateUser(@PathVariable("usersId") Long id, Model model){
        model.addAttribute("user", id == 0 ? new User() : userRepositoryService.getUserById(id));
        return "users/user_form";
    }

}
