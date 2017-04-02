package com.codecool.eshipdiary.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetController.class);

    @RequestMapping(value = {"forgot-password"})
    public String getForgotPasswordPage() {
        return "password/forgot_password";
    }

    @RequestMapping(value = "/password-reset", method = RequestMethod.POST)
    public String sendEmailWithPasswordResetToken(@RequestParam("email") String userEmail){
        LOG.info("Password reset requested for email address: " + userEmail);
        return "redirect:/login";
    }
}
