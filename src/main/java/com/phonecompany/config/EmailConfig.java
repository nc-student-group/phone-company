package com.phonecompany.config;

import com.phonecompany.service.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class EmailConfig {

    @Value("${available.email.threads}")
    public static Integer NUMBER_OF_AVAILABLE_THREADS;

    @Value("${mail.host}")
    private String mailHost;
    @Value("${mail.port}")
    private String mailPort;
    @Value("${mail.address}")
    private String senderEmail;
    @Value("${mail.password}")
    private String senderPassword;

    /**
     * Defines {@code JavaMailSenderIml} bean.
     *
     * @return {@code JavaMailSender} bean.
     * @see EmailService
     */
    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(this.mailHost);
        javaMailSender.setPort(Integer.parseInt(this.mailPort));
        javaMailSender.setUsername(this.senderEmail);
        javaMailSender.setPassword(this.senderPassword);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.sendpartial", "true");

        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }
}

