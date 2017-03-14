package com.codecool.eshipdiary.service;

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

    public MimeMessagePreparator prepareRegistrationEmail(String recipient, String message) {
        LOG.debug("Recipient {} and message {}", recipient, message);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(recipient);
            messageHelper.setSubject("Sikeres regisztráció");
            messageHelper.setText(message);
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
