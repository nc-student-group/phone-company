package com.phonecompany.service;

import com.phonecompany.service.interfaces.EMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

/**
 * Created by nik9str on 15.04.2017.
 */
@Service
@PropertySource("classpath:mail.properties")
public class EMailServiceImpl implements EMailService {

    @Autowired
    private Environment environment;

    private JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty("email.host"));
        mailSender.setPort(Integer.parseInt(environment.getProperty("email.port")));
        mailSender.setUsername(environment.getProperty("email.address"));
        mailSender.setPassword(environment.getProperty("email.password"));
        mailSender.setProtocol(environment.getProperty("email.protocol"));
        return mailSender;
    }


    @Override
    public void sendMail(String recipientEmail, String text, String subject) {
        JavaMailSender mailSender = getJavaMailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(environment.getProperty("email.address"));
        try {
            mailSender.send(message);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
