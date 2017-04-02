package com.codecool.eshipdiary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PasswordResetController {
    @RequestMapping(value = {"forgot-password"})
    public String getForgotPasswordPage() {
        return "password/forgot_password";
    }
}
