package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.exception.service_layer.KeyAlreadyPresentException;
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
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> getAllUsersPaging(int page, int size, int roleId, String status, String email,
                                                 int orderBy, String orderByType) {
        Query query = buildQueryForUsersTable(page, size, roleId, status, email, orderBy, orderByType);
        Map<String, Object> response = new HashMap<>();
        response.put("users", this.userDao.executeForList(query.getQuery(), query.getPreparedStatementParams().toArray()));
        response.put("usersSelected", this.userDao.executeForInt(query.getCountQuery(), query.getCountParams().toArray()));
        return response;
    }

    private Query buildQueryForUsersTable(int page, int size, int roleId, String status, String email,
                                          int orderBy, String orderByType) {
        Query.Builder builder = new Query.Builder("dbuser");
        builder.where().addCondition("dbuser.role_id <> ?", 4).and().addCondition("dbuser.role_id <> ?", 1)
                .and().addLikeCondition("dbuser.email", email);
        if (roleId > 0) builder.and().addCondition("dbuser.role_id = ?", roleId);
        if (!status.equals("ALL")) builder.and().addCondition("dbuser.status = ?", status);
        String orderByField = buildOrderBy(orderBy);
        if (orderByField.length() > 0) {
            builder.orderBy(orderByField);
            builder.orderByType(orderByType);
        }
        builder.addPaging(page, size);
        return builder.build();
    }

    private String buildOrderBy(int orderBy) {
        switch (orderBy) {
            case 0://by email
                return "dbuser.email";
            case 1://by role
                return "dbuser.role_id";
            case 2://by status
                return "dbuser.status";
            default:
                return "";
        }
    }


    @Override
    public void changePassword(String oldPass, String newPass, User user) {
        if (shaPasswordEncoder.encodePassword(oldPass, null).equals(user.getPassword())) {
            user.setPassword(shaPasswordEncoder.encodePassword(newPass, null));
            userDao.update(user);
        } else {
            throw new ConflictException("Old password is wrong");
        }
    }

    //TODO: untestable method. does not return anything
    @Override
    public void updateStatus(long id, Status status) {
        userDao.updateStatus(id, status);
    }

    @Override
    public List<User> getAllUsersSearch(int page, int size, String email, int userRole, String status) {
        Query.Builder query = new Query.Builder("dbuser");
        query.where().addLikeCondition("email", email);

        if (userRole > 0) {
            query.and().addCondition("role_id = ?", userRole);
        } else if (userRole != 0) {
            throw new ConflictException("Incorrect search parameter: user role.");
        }

        if (!status.equals("ALL")) {
            query.and().addCondition("status = ?", status);
        }
        query.addPaging(page, size);
        return userDao.getAllUsersSearch(query.build());
    }

    @Override
    public int getCountSearch(int page, int size, String email, int userRole, String status) {
        Query.Builder query = new Query.Builder("dbuser");
        query.where().addLikeCondition("email", email);

        if (userRole > 0) {
            query.and().addCondition("role_id = ?", userRole);
        } else if (userRole != 0) {
            throw new ConflictException("Incorrect search parameter: user role.");
        }
        if (!status.equals("ALL")) {
            query.and().addCondition("status = ?", status);
        }
        return userDao.getAllUsersSearch(query.build()).size();
    }
}