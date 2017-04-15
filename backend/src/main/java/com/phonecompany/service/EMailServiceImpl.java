package com.phonecompany.service;

import com.phonecompany.service.interfaces.EMailService;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;


/**
 * Created by nik9str on 15.04.2017.
 */
@Service
public class EMailServiceImpl implements EMailService {

    private String host = "smtp.gmail.com";
    private int port = 465;
    private String email ="nc.phone.company.project@gmail.com";
    private String password ="99ecc04560666945bc95824b30d33b575478c323076284b6e4ea39362633dd4d";
    private String protocol ="smtps";

    private JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(email);
        mailSender.setPassword(password);
        mailSender.setProtocol(protocol);
        return mailSender;
    }


    @Override
    public void sendMail(String recipientEmail,String text, String subject) {
        JavaMailSender mailSender = getJavaMailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(email);
        try {
            mailSender.send(message);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
