package com.phonecompany.service;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.EmailAlreadyPresentException;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.MailMessageCreator;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserServiceImpl extends AbstractUserServiceImpl<User>
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
        this.userDao = userDao;
        this.shaPasswordEncoder = shaPasswordEncoder;
        this.resetPassMessageCreator = resetPassMessageCreator;
        this.emailService = emailService;
        this.confirmMessageCreator = confirmMessageCreator;
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
}