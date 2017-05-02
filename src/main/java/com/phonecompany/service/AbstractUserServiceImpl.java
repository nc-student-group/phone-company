package com.phonecompany.service;

import com.phonecompany.exception.EmailAlreadyPresentException;
import com.phonecompany.model.Customer;
import com.phonecompany.model.User;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.events.ResetPasswordEvent;
import com.phonecompany.service.interfaces.AbstractUserService;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
public abstract class AbstractUserServiceImpl<T extends User>
        extends CrudServiceImpl<T> implements AbstractUserService<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractUserServiceImpl.class);

    @Autowired
    private ShaPasswordEncoder shaPasswordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    @Qualifier("resetPassMessageCreator")
    private MailMessageCreator<User> resetPassMessageCreator;
    @Autowired
    @Qualifier("confirmationEmailCreator")
    private MailMessageCreator<VerificationToken> confirmMessageCreator;

    public AbstractUserServiceImpl() {
    }

    @Override
    @EventListener
    public T resetPassword(ResetPasswordEvent<T> resetPasswordEvent) {
        T userToReset = resetPasswordEvent.getUserToReset();
        Assert.notNull(userToReset, "User should not be null");
        userToReset.setPassword(generatePassword());
        LOG.info("Sending password reset email to: {}", userToReset.getEmail());
        SimpleMailMessage mailMessage =
                this.resetPassMessageCreator.constructMessage(userToReset);
        emailService.sendMail(mailMessage, userToReset);
        LOG.info("Resetting password");
        userToReset.setPassword(shaPasswordEncoder.encodePassword(userToReset.getPassword(), null));
        LOG.info("Password after reset: {}", userToReset.getPassword());
        return update(userToReset);
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(50, random).toString(32);
    }

    @Override
    public T save(T entity) {
        Assert.notNull(entity, "User should not be null");
        User userByEmail = this.findByEmail(entity.getEmail());
        if(userByEmail != null) {
            throw new EmailAlreadyPresentException(entity.getEmail());
        }
        entity.setStatus(this.getStatus());
        entity.setPassword(shaPasswordEncoder.encodePassword(entity.getPassword(), null));
        return super.save(entity);
    }

    public abstract Status getStatus();
}
