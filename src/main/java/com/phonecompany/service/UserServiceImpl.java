package com.phonecompany.service;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.SecuredUser;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.model.events.OnUserCreationEvent;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

@Service
public class UserServiceImpl extends AbstractUserServiceImpl<User>
        implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDao userDao;
    private ShaPasswordEncoder shaPasswordEncoder;
    private EmailService emailService;
    private MailMessageCreator<User> resetPassMessageCreator;
    private MailMessageCreator<VerificationToken> confirmMessageCreator;
    private MailMessageCreator<User> passwordAssignmentCreator;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           ShaPasswordEncoder shaPasswordEncoder,
                           @Qualifier("resetPassMessageCreator")
                                   MailMessageCreator<User> resetPassMessageCreator,
                           @Qualifier("confirmationEmailCreator")
                                   MailMessageCreator<VerificationToken> confirmMessageCreator,
                           @Qualifier("passwordAssignmentMessageCreator")
                                   MailMessageCreator<User> passwordAssigmentCreator,
                           EmailService emailService) {
        this.userDao = userDao;
        this.shaPasswordEncoder = shaPasswordEncoder;
        this.resetPassMessageCreator = resetPassMessageCreator;
        this.emailService = emailService;
        this.confirmMessageCreator = confirmMessageCreator;
        this.passwordAssignmentCreator = passwordAssigmentCreator;
    }

    @EventListener
    public void confirmRegistration(OnUserCreationEvent onUserCreationEvent) {
        User persistedUser = onUserCreationEvent.getPersistedUser();
        SimpleMailMessage confirmationMessage =
                this.passwordAssignmentCreator.constructMessage(persistedUser);
        LOG.info("Sending email confirmation message to: {}", persistedUser.getEmail());
        emailService.sendMail(confirmationMessage, persistedUser);
    }

    @Override
    public User findByEmail(String email) {
        Assert.notNull(email, "Email should not be null");
        return userDao.findByEmail(email);
    }

    @Override
    public Status getStatus() {
        return Status.ACTIVATED;
    }

    @Override
    public User update(User user) {
        Assert.notNull(user, "User should not be null");

        return super.update(user);
    }

    @Override
    public User resetPassword(User user) {
        user.setPassword(generatePassword());
        sendResetPasswordMessage(user);
        user.setPassword(shaPasswordEncoder.encodePassword(user.getPassword(), null));
        return update(user);
    }

    private void sendResetPasswordMessage(User user) {
        SimpleMailMessage resetPasswordMessage =
                this.resetPassMessageCreator.constructMessage(user);
        LOG.info("Sending email reset password to: {}", user.getEmail());
        emailService.sendMail(resetPasswordMessage, user);
    }

    public String generatePassword() {
        SecureRandom random = new SecureRandom();
        String password = new BigInteger(50, random).toString(32);
        char[] specSymb = "!@$".toCharArray();
        char[] passwordWithSS = password.toCharArray();
        passwordWithSS[random.nextInt(passwordWithSS.length)] = specSymb[random.nextInt(specSymb.length)];
        password = String.valueOf(passwordWithSS);
        return password;
    }

    public User getCurrentlyLoggedInUser() {
        SecuredUser securedUser = new SecuredUser(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        User user = this.findByEmail(securedUser.getUserName());
        LOG.debug("User retrieved from the security context: {}", user);
        return user;
    }

    @Override
    public List<User> getAllUsersPaging(int page, int size, int roleId, String status) {
        return userDao.getPaging(page, size, roleId, status);
    }

    @Override
    public int getCountUsers(int roleId, String status) {
        return userDao.getEntityCount(roleId, status);
    }
}