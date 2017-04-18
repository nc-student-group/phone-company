package com.phonecompany.service;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.OnRegistrationCompleteEvent;
import com.phonecompany.model.ResetPasswordEvent;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.MailMessageCreator;
import com.phonecompany.service.interfaces.UserService;
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
public class UserServiceImpl extends CrudServiceImpl<User>
        implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDao userDao;
    private ShaPasswordEncoder shaPasswordEncoder;
    private EmailService emailService;
    private MailMessageCreator<User> resetPassMessageCreator;
    private MailMessageCreator<User> confirmMessageCreator;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           ShaPasswordEncoder shaPasswordEncoder,
                           @Qualifier("resetPassMessageCreator")
                                   MailMessageCreator<User> resetPassMessageCreator,
                           @Qualifier("confirmationEmailCreator")
                                       MailMessageCreator<User> confirmMessageCreator,
                           EmailService emailService) {
        super(userDao);
        this.userDao = userDao;
        this.shaPasswordEncoder = shaPasswordEncoder;
        this.resetPassMessageCreator = resetPassMessageCreator;
        this.emailService = emailService;
        this.confirmMessageCreator = confirmMessageCreator;
    }

    public static void main(String[] args) {
        String pass = "6k1ff7u9ak";
        ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();
        String encodedPassword = shaPasswordEncoder.encodePassword(pass, null);
        System.out.println(encodedPassword);
    }

    @Override
    @EventListener
    public User resetPassword(ResetPasswordEvent resetPasswordEvent) {
        User userToReset = resetPasswordEvent.getUserToReset();
        Assert.notNull(userToReset, "User should not be null");
        userToReset.setPassword(generatePassword());
        LOG.info("Sending password reset email to: {}", userToReset.getEmail());
        SimpleMailMessage mailMessage = this.resetPassMessageCreator.constructMessage(userToReset);
        emailService.sendMail(mailMessage);
        LOG.info("Resetting password");
        userToReset.setPassword(shaPasswordEncoder.encodePassword(userToReset.getPassword(), null));
        LOG.info("Password after reset: {}", userToReset.getPassword());
//        userToReset.setPassword(encryptPassword(userToReset.getPassword()));
        return update(userToReset);
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(50, random).toString(32);
    }

    @Override
    @EventListener
    public void confirmRegistration(OnRegistrationCompleteEvent registrationCompleteEvent) {
        User persistedUser = registrationCompleteEvent.getPersistedUser();
        SimpleMailMessage confirmationMessage =
                this.confirmMessageCreator.constructMessage(persistedUser);
        LOG.info("Sending email confirmation message to: {}", persistedUser.getEmail());
        emailService.sendMail(confirmationMessage);
    }

    @Override
    public User findByUsername(String userName) {
        Assert.notNull(userName, "Username should not be null");
        return userDao.findByEmail(userName);
    }

    @Override
    public User findByEmail(String email) {
        Assert.notNull(email, "Email should not be null");
        return userDao.findByEmail(email);
    }

    public User save(User user) {
        Assert.notNull(user, "User should not be null");
        user.setPassword(shaPasswordEncoder.encodePassword(user.getPassword(), null));
        return super.save(user);
    }

    public User update(User user) {
        Assert.notNull(user, "User should not be null");

        return super.update(user);
    }

    @Override
    public String encryptPassword(String password){
        return shaPasswordEncoder.encodePassword(password, null);
    }

}