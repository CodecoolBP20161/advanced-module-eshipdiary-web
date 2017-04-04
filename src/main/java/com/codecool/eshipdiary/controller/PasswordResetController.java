package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.UserRepository;
import com.codecool.eshipdiary.service.EmailService;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value = "/forgot-password")
    public String getForgotPasswordPage() {
        return "password/forgot_password";
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public String sendEmailWithPasswordResetToken(HttpServletRequest request, @RequestParam("emailAddress") String userEmail){
        LOG.info("Password reset requested for email address: " + userEmail);
        Optional<User> user = userRepository.findOneByEmailAddress(userEmail);
        if (!user.isPresent()) {
            return "redirect:/login";
//            TODO: do something, e.g. throw new UserNotFoundException();
        }
        User match = user.get();
        String token = UUID.randomUUID().toString(); // generate token
        String link = getAppUrl(request) + "/reset-password?id=" + match.getId() + "&token=" + token;
        LOG.info("constructed password reset link: " + link);

        userService.createPasswordResetTokenForUser(match, token); // save token to user in DB
        emailService.sendEmail(emailService.prepareRegistrationEmail(match, link));
        return "redirect:/login";
    }

    @RequestMapping(value = "/reset-password")
    public String resetPassword(@RequestParam("id") Long id, @RequestParam("token") String token) {
        LOG.info("A password reset link has been clicked with token: " + token);
        return "password/reset_password";
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
