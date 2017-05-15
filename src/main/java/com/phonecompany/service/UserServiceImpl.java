package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.exception.KeyAlreadyPresentException;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.events.OnUserCreationEvent;
import com.phonecompany.service.email.PasswordAssignmentEmailCreator;
import com.phonecompany.service.email.ResetPasswordEmailCreator;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.UserService;
import com.phonecompany.util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

@ServiceStereotype
public class UserServiceImpl extends AbstractUserServiceImpl<User>
        implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${application-url}")
    private String applicationUrl;

    private UserDao userDao;
    private ShaPasswordEncoder shaPasswordEncoder;
    private EmailService<User> emailService;
    private ResetPasswordEmailCreator resetPassMessageCreator;
    private PasswordAssignmentEmailCreator passwordAssignmentCreator;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           ShaPasswordEncoder shaPasswordEncoder,
                           ResetPasswordEmailCreator resetPassMessageCreator,
                           PasswordAssignmentEmailCreator passwordAssignmentCreator,
                           EmailService<User> emailService) {
        this.userDao = userDao;
        this.shaPasswordEncoder = shaPasswordEncoder;
        this.resetPassMessageCreator = resetPassMessageCreator;
        this.emailService = emailService;
        this.passwordAssignmentCreator = passwordAssignmentCreator;
    }

    @Override
    @EventListener
    public void sendConfirmationEmail(OnUserCreationEvent onUserCreationEvent) {
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
    public void validate(User user) {
        String email = user.getEmail();
        int countByEmail = this.userDao.getCountByEmail(email);
        if (countByEmail != 0) {
            throw new KeyAlreadyPresentException(email);
        }
    }

    @Override
    public User update(User user) {
        Assert.notNull(user, "User should not be null");

        return super.update(user);
    }

    //TODO: extract email dispatch to controller
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

    @Override
    public List<User> getAllUsersPaging(int page, int size, int roleId, String status) {
        return userDao.getPaging(page, size, roleId, status);
    }

    //TODO: we should not know currently logged in user at the service layer
    @Override
    public void changePassword(String oldPass, String newPass) {
        User user = this.getCurrentlyLoggedInUser();
        if (shaPasswordEncoder.encodePassword(oldPass, null).equals(user.getPassword())) {
            user.setPassword(shaPasswordEncoder.encodePassword(newPass, null));
            userDao.update(user);
        } else {
            throw new ConflictException("Old password is wrong");
        }
    }

    //TODO: method is called "getCountUsers" but accepts status as a parameter
    //TODO: rename to getUserCountByStatus ?
    @Override
    public int getCountUsers(int roleId, String status) {
        return userDao.getEntityCount(roleId, status);
    }

    //TODO: untestable method. does not return anything
    @Override
    public void updateStatus(long id, Status status) {
        userDao.updateStatus(id, status);
    }

    @Override
    public List<User> getAllUsersSearch(String email, int userRole, String status) {
        Query query;
        if(userRole==0) {
            if(status.equals("ALL")){
                query = new Query.Builder("dbuser").where().addLikeCondition("email", email).build();
            }else if(status.equals("ACTIVATED") ||status.equals("DEACTIVATED")){
                query = new Query.Builder("dbuser").where().addLikeCondition("email", email)
                        .and().addCondition("status=?", status).build();
            }else{
                throw new ConflictException("Incorrect search parameter: status.");
            }
        }else if (userRole>0 && userRole<5) {
            if(status.equals("ALL")){
                query = new Query.Builder("dbuser").where().addLikeCondition("email",email)
                        .and().addCondition("role_id=?",userRole).build();
            }else if(status.equals("ACTIVATED") ||status.equals("DEACTIVATED")){
                query = new Query.Builder("dbuser").where().addLikeCondition("email",email)
                        .and().addCondition("role_id=?",userRole).and().addCondition("status=?",status).build();
            }else{
                throw new ConflictException("Incorrect search parameter: status.");
            }
        }else{
            throw new ConflictException("Incorrect search parameter: user role.");
        }
        return userDao.getAllUsersSearch(query);
    }
}