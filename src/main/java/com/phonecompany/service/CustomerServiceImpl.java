package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.model.Customer;
import com.phonecompany.model.events.OnRegistrationCompleteEvent;
import com.phonecompany.model.enums.Status;
import com.phonecompany.service.interfaces.CustomerService;
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

import java.util.HashMap;
import java.util.List;

@Service
public class CustomerServiceImpl extends AbstractUserServiceImpl<Customer>
        implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private CustomerDao customerDao;
    private ShaPasswordEncoder shaPasswordEncoder;
    private MailMessageCreator<Customer> resetPassMessageCreator;
    private MailMessageCreator<Customer> confirmMessageCreator;
    private EmailService emailService;

    @Autowired
    public CustomerServiceImpl(CrudDao<Customer> customerDao,
                               ShaPasswordEncoder shaPasswordEncoder,
                               @Qualifier("resetPassMessageCreator")
                                       MailMessageCreator<Customer> resetPassMessageCreator,
                               @Qualifier("confirmationEmailCreator")
                                       MailMessageCreator<Customer> confirmMessageCreator,
                               EmailService emailService) {
        this.customerDao = (CustomerDao) customerDao;
        this.shaPasswordEncoder = shaPasswordEncoder;
        this.resetPassMessageCreator = resetPassMessageCreator;
        this.confirmMessageCreator = confirmMessageCreator;
        this.emailService = emailService;
    }

    @Override
    public Status getStatus() {
        return Status.INACTIVE;
    }

    @Override
    @EventListener
    public void confirmRegistration(OnRegistrationCompleteEvent registrationCompleteEvent) {
        Customer persistedCustomer = registrationCompleteEvent.getPersistedUser();
        SimpleMailMessage confirmationMessage =
                this.confirmMessageCreator.constructMessage(persistedCustomer);
        LOG.info("Sending email confirmation message to: {}", persistedCustomer.getEmail());
        emailService.sendMail(confirmationMessage);
    }

    @Override
    public void activateUserByToken(String token) {
        Customer customer = this.customerDao.getByVerificationToken(token);
        LOG.debug("Customer fetched by verification token: {}", customer);
        customer.setStatus(Status.ACTIVATED);
        this.customerDao.update(customer);
        LOG.debug("User has been activated");
    }

    @Override
    public Customer findByEmail(String userName) {
        Assert.notNull(userName, "Username should not be null");
        return customerDao.findByEmail(userName);
    }

    @Override
    public Customer update(Customer user) {
        Assert.notNull(user, "User should not be null");

        return super.update(user);
    }

    @Override
    public List<Customer> getAllCustomersPaging(int page, int size, long regionId, String status) {
        return customerDao.getPaging(page, size, regionId, status);
    }

    @Override
    public int getCountCustomers(long regionId, String status) {
        return customerDao.getEntityCount(regionId, status);
    }
}

