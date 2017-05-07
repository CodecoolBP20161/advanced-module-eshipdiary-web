package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.UserRepository;
import com.codecool.eshipdiary.service.EmailService;
import com.codecool.eshipdiary.service.PasswordTokenRepositoryService;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordResetController {
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRepositoryService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordTokenRepositoryService passwordTokenRepositoryService;

    @RequestMapping(value = "/forgot-password")
    public String getForgotPasswordPage() {
        return "password/forgot_password";
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public String sendEmailWithPasswordResetToken(HttpServletRequest request, @RequestParam("emailAddress") String userEmail){
        LOG.info("Password reset requested for email address: " + userEmail);
        Optional<User> user = userRepository.findOneByEmailAddress(userEmail);
        if (!user.isPresent()) {
            LOG.info("No user found with email address: " + userEmail);
        } else {
            User match = user.get();
            String token = UUID.randomUUID().toString(); // generate token
            String link = getAppUrl(request) + "/reset-password/" + token;
            LOG.info("constructed password reset link: " + link);

            userService.createPasswordResetTokenForUser(match, token); // save token to user in DB
            emailService.sendEmail(emailService.prepareForgotPasswordEmail(match, link));
        }
        return "redirect:/login?forgot";
    }

    @RequestMapping(value = "/reset-password/{token}")
    public String resetPassword(@PathVariable("token") String token) {
        if(passwordTokenRepositoryService.getTokenByToken(token).isPresent()){
            LOG.info("A password reset link has been clicked with token: " + token);
            return "password/reset_password";
        } else {
            LOG.info("Invalid token: " + token);
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/reset-password/{token}", method = RequestMethod.POST)
    public String createOar(@PathVariable("token") String token, @RequestParam("password") String password){
        try {
            User user = passwordTokenRepositoryService.getTokenByToken(token).get().getUser();
            user.setPasswordHash(password);
            userRepository.save(user);
            LOG.info("Password has been reset for user: " + user.getUsername());
        } catch (Exception e) {
            LOG.info("Invalid token: " + token);
        }
        return "redirect:/login?reset";
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
