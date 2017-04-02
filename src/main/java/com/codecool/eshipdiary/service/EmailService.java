package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private EmailContentBuilder emailContentBuilder;

    public MimeMessagePreparator prepareRegistrationEmail(User user, String link) {
        LOG.debug("Sending email to the following user {}", user.toString());
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setTo(user.getEmailAddress());
            messageHelper.setSubject("Sikeres regisztráció");
            String content = emailContentBuilder.build(user, link);
            messageHelper.setText(content, true);
        };

        return messagePreparator;
    }

    @Async
    public void sendEmail(MimeMessagePreparator email) {
        try {
            emailSender.send(email);
            LOG.info("{} - Email sent successfully", new Date());
        } catch (MailException e) {
            LOG.error("Error trying to send email on {} due to {}", new Date(), e.getMessage());
        }
    }
}
