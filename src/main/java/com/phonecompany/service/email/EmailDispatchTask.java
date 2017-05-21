package com.phonecompany.service.email;

import com.phonecompany.exception.service_layer.MailMessageConstructionException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * This class allows an email dispatch process to be fired up
 * in a separate thread
 */
public class EmailDispatchTask implements Runnable {

    static final String PHONE_COMPANY_EMAIL = "nc.phone.company.project@gmail.com";

    private JavaMailSender mailSender;
    private SimpleMailMessage mailMessage;

    /**
     * Specifies an implementation that will be used for
     * the message dispatch as well as the message itself
     *
     * @param mailSender  mail sender implementation
     * @param mailMessage message to be sent
     */
    public EmailDispatchTask(JavaMailSender mailSender,
                             SimpleMailMessage mailMessage) {
        this.mailSender = mailSender;
        this.mailMessage = mailMessage;
    }

    /**
     * Performs a message dispatch
     *
     * @see com.phonecompany.config.EmailConfig
     * @see EmailServiceImpl
     */
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
            throw new MailMessageConstructionException(e);
        }
    }
}
