package com.phonecompany.service.email;

import com.phonecompany.exception.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailDispatchTask implements Runnable {

    public static final String PHONE_COMPANY_EMAIL = "nc.phone.company.project@gmail.com";

    private JavaMailSender mailSender;
    private SimpleMailMessage mailMessage;

    public EmailDispatchTask(JavaMailSender mailSender,
                             SimpleMailMessage mailMessage) {
        this.mailSender = mailSender;
        this.mailMessage = mailMessage;
    }

    @Override
    public void run() {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setText(mailMessage.getText(), true); // true = isHtml
            mimeMessageHelper.setTo(mailMessage.getTo());
            mimeMessageHelper.setSubject(mailMessage.getSubject());
            mimeMessageHelper.setFrom(PHONE_COMPANY_EMAIL);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new MailSendException(e);
        }
    }
}
