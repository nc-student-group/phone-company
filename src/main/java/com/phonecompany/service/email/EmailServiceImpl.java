package com.phonecompany.service.email;

import com.phonecompany.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EmailServiceImpl implements EmailService {

    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(SimpleMailMessage mailMessage) {
        executorService.execute(new EmailDispatchTask(mailSender, mailMessage));
    }
}
