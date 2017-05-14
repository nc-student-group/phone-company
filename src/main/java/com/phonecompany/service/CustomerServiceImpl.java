package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.KeyAlreadyPresentException;
import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.events.OnRegistrationCompleteEvent;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.MailMessageCreator;
import com.phonecompany.service.interfaces.VerificationTokenService;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class CustomerServiceImpl extends AbstractUserServiceImpl<Customer>
        implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(UserActions.class);

    @Value("${application-url}")
    private String applicationUrl;

    private ServiceDao serviceDao;
    private CustomerDao customerDao;
    private VerificationTokenService verificationTokenService;
    private MailMessageCreator<VerificationToken> confirmMessageCreator;
    private EmailService<Customer> emailService;
    private TariffService tariffService;
    private CustomerTariffService customerTariffService;

    @Autowired
    public CustomerServiceImpl(ServiceDao serviceDao,
                               CustomerDao customerDao,
                               VerificationTokenService verificationTokenService,
                               @Qualifier("confirmationEmailCreator")
                                       MailMessageCreator<VerificationToken> confirmMessageCreator,
                               EmailService<Customer> emailService,
                               TariffService tariffService,
                               CustomerTariffService customerTariffService) {
        this.serviceDao = serviceDao;
        this.customerDao = customerDao;
        this.verificationTokenService = verificationTokenService;
        this.confirmMessageCreator = confirmMessageCreator;
        this.emailService = emailService;
        this.tariffService = tariffService;
        this.customerTariffService = customerTariffService;
    }

    @Override
    public Status getStatus() {
        return Status.INACTIVE;
    }

    @Override
    public void validate(Customer customer) {
        Assert.notNull(customer);
        String phone = customer.getPhone();
        int countByPhone = this.customerDao.getCountByPhone(phone);
        if (countByPhone != 0) {
            throw new KeyAlreadyPresentException(phone);
        }
        String email = customer.getEmail();
        int countByEmail = this.customerDao.getCountByEmail(email);
        if (countByEmail != 0) {
            throw new KeyAlreadyPresentException(email);
        }
    }

    @Override
    @EventListener
    public void confirmRegistration(OnRegistrationCompleteEvent registrationCompleteEvent) {
        Customer persistedCustomer = registrationCompleteEvent.getPersistedUser();

        VerificationToken persistedToken = verificationTokenService
                .saveTokenForUser(persistedCustomer);

        SimpleMailMessage confirmationMessage =
                this.confirmMessageCreator.constructMessage(persistedToken);
        LOG.info("Sending email confirmation message to: {}", persistedCustomer.getEmail());
        emailService.sendMail(confirmationMessage, persistedCustomer);
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
        Customer notUpdatedCustomer = this.getById(user.getId());
        LOG.info(notUpdatedCustomer.toString());
        LOG.info(user.toString());
        if (notUpdatedCustomer.getCorporate() != null && user.getCorporate() != null) {
            if ((!notUpdatedCustomer.getCorporate().getId().equals(user.getCorporate().getId())) || (!notUpdatedCustomer.getAddress().getRegion().getId().equals(user.getAddress().getRegion().getId()))) {
                this.deactivateCustomerTariff(user.getId());
            }
        } else if (notUpdatedCustomer.getCorporate() != null && user.getCorporate() == null) {
            this.deactivateCustomerTariff(user.getId());
        } else if (notUpdatedCustomer.getCorporate() == null && user.getCorporate() != null) {
            this.deactivateCustomerTariff(user.getId());
        } else if ((!notUpdatedCustomer.getAddress().getRegion().getId().equals(user.getAddress().getRegion().getId()))) {
            this.deactivateCustomerTariff(user.getId());
        }
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

    @Override
    public void deactivateCustomer(long id) {

    }

    @Override
    public List<Customer> getCustomersByCorporate(long corporateId) {
        return customerDao.getByCorporateId(corporateId);
    }

    @Override
    public void updateStatus(long id, Status status) {
        customerDao.updateStatus(id, status);
    }

    @Override
    public void deactivateCustomerTariff(long id) {
        Customer customer = this.getById(id);
        if (customer.getCorporate() == null) {
            CustomerTariff customerTariff = customerTariffService.getCurrentCustomerTariff(id);
            if (customerTariff != null) {
                tariffService.deactivateSingleTariff(customerTariff);
            }
        } else {
            //TODO DELETE CUSTOMER FROM COMPANY???
        }
    }

}

