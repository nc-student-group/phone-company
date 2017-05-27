package com.phonecompany.service.email;

import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl<T extends User> implements EmailService<T> {

    private JavaMailSender mailSender;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(SimpleMailMessage mailMessage,
                         List<T> recipients) {
        mailMessage.setTo(this.getArrayOfEmailRecipients(recipients));
        executorService.execute(new EmailDispatchTask(mailSender, mailMessage));
    }


    private String[] getArrayOfEmailRecipients(List<T> recipients) {
        String[] arrayOfRecipientEmails = new String[recipients.size()];

        return recipients.stream()
                .map(User::getEmail)
                .collect(Collectors.toList())
                .toArray(arrayOfRecipientEmails);
    }

    /**
     * Submits an email dispatch task to be fired up in one of the
     * available threads
     *
     * @param mailMessage message to be sent
     */
    @Override
    public void sendMail(SimpleMailMessage mailMessage, T recipient) {
        mailMessage.setTo(recipient.getEmail());
        executorService.execute(new EmailDispatchTask(mailSender, mailMessage));
    }
}
