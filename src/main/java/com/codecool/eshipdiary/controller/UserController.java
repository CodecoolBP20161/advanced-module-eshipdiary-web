package com.codecool.eshipdiary.controller;


import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.model.ValidationResponse;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.sql.SQLException;


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

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public
    @ResponseBody ValidationResponse saveNewUser(@ModelAttribute("user") @Valid User newUser, BindingResult result) {
        ValidationResponse response = new ValidationResponse();

        if (!result.hasErrors()) {
            response.setStatus("SUCCESS");
        }

        try {
            userRepositoryService.create(newUser);
        } catch (DataIntegrityViolationException | ConstraintViolationException | SQLException e) {
            response.setStatus("FAIL");
            response.setResult("Nem várt hiba: " + e.getMessage());
            LOG.error("Could not save new user", e.getMessage());

            if(userRepositoryService.getUserByUserName(newUser.getUserName()).isPresent()) {
                response.setResult("Foglalt felhasználónév");
            }
            if(userRepositoryService.getUserByEmail(newUser.getEmailAddress()).isPresent()) {
                response.setResult("A megadott email-címmel már van regisztrált tag");
            }
        }

        return response;
    }
}
