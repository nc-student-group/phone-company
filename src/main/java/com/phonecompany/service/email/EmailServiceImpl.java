package com.phonecompany.service.email;

import com.phonecompany.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@PropertySource("classpath:mail.properties")
public class EmailServiceImpl implements EmailService {

    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Submits an email dispatch task to be fired up in one of the
     * available threads provided by thread pool provider
     *
     * @param mailMessage message to be sent
     */
    @Override
    public void sendMail(SimpleMailMessage mailMessage) {
        executorService.execute(new EmailDispatchTask(mailSender, mailMessage));
    }
}
