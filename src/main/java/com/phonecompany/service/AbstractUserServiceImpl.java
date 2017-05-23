package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.model.SecuredUser;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.events.ResetPasswordEvent;
import com.phonecompany.service.email.customer_related_emails.ResetPasswordEmailCreator;
import com.phonecompany.service.interfaces.AbstractUserService;
import com.phonecompany.service.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.security.SecureRandom;

@ServiceStereotype
public abstract class AbstractUserServiceImpl<T extends User>
        extends CrudServiceImpl<T> implements AbstractUserService<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractUserServiceImpl.class);

    @Autowired
    private ShaPasswordEncoder shaPasswordEncoder;
    @Autowired
    private EmailService<User> emailService;
    @Autowired
    private ResetPasswordEmailCreator resetPassMessageCreator;

    public AbstractUserServiceImpl() {
    }

    public T getCurrentlyLoggedInUser() {
        SecuredUser securedUser = new SecuredUser(SecurityContextHolder
                .getContext().getAuthentication().getPrincipal());
        T userFoundByEmail = this.findByEmail(securedUser.getUserName());
        LOG.debug("User retrieved from the security context: {}", userFoundByEmail);
        return userFoundByEmail;
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(50, random).toString(32);
    }

    @Override
    public T save(T entity) {
        this.validate(entity);
        entity.setStatus(this.getStatus());
        entity.setPassword(shaPasswordEncoder.encodePassword(entity.getPassword(), null));
        return super.save(entity);
    }

    /**
     * Performs a set of validating operations particular to the given
     * entity.
     *
     * @param entity entity to be validated
     */
    public abstract void validate(T entity);

    /**
     * Defines a default status that entities of the given type are
     * being created with.
     *
     * @return default status
     */
    public abstract Status getStatus();
}
