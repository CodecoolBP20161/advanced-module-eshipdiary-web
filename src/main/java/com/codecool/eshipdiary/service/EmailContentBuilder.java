package com.codecool.eshipdiary.service;


import com.codecool.eshipdiary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailContentBuilder {

    @Autowired
    private TemplateEngine templateEngine;

    public String build(User user, String link) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("link", link);
        return templateEngine.process("email/registration_email", context);
    }
}

