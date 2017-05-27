package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.Address;
import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;
import com.phonecompany.service.email.customer_related_emails.PasswordAssignmentEmailCreator;
import com.phonecompany.service.interfaces.*;
import com.phonecompany.util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phonecompany.model.enums.Status.ACTIVATED;

@ServiceStereotype
public class CustomerServiceImpl extends AbstractUserServiceImpl<Customer>
        implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(UserActions.class);

    private CustomerDao customerDao;
    private TariffService tariffService;
    private CustomerTariffService customerTariffService;
    private ShaPasswordEncoder shaPasswordEncoder;
    private AddressService addressService;
    private EmailService<Customer> emailService;
    private PasswordAssignmentEmailCreator passwordAssignmentEmailCreator;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao,
                               TariffService tariffService,
                               CustomerTariffService customerTariffService,
                               ShaPasswordEncoder shaPasswordEncoder,
                               AddressService addressService,
                               EmailService<Customer> emailService,
                               PasswordAssignmentEmailCreator passwordAssignmentEmailCreator) {
        this.customerDao = customerDao;
        this.tariffService = tariffService;
        this.customerTariffService = customerTariffService;
        this.shaPasswordEncoder = shaPasswordEncoder;
        this.addressService = addressService;
        this.emailService = emailService;
        this.passwordAssignmentEmailCreator = passwordAssignmentEmailCreator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status getStatus() {
        return Status.INACTIVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Customer customer) {
        Assert.notNull(customer);
        String phone = customer.getPhone();
        int countByPhone = this.customerDao.getCountByPhone(phone);
        if (countByPhone != 0) {
            throw new ConflictException("User associated with " + phone + " already exists");
        }
        String email = customer.getEmail();
        int countByEmail = this.customerDao.getCountByEmail(email);
        if (countByEmail != 0) {
            throw new ConflictException("User associated with " + email + " already exists");
        }
    }

    @Override
    public Customer save(Customer customer) {
        customer.setRole(UserRole.CLIENT);
        return super.save(customer);
    }

    public Customer saveByAdmin(Customer customer) {
        if (this.findByEmail(customer.getEmail()) == null) {
            customer.setPassword(new BigInteger(50, new SecureRandom()).toString(32));
            SimpleMailMessage confirmationMessage =
                    this.passwordAssignmentEmailCreator.constructMessage(customer);
            LOG.info("Sending email confirmation message to: {}", customer.getEmail());
            emailService.sendMail(confirmationMessage, customer);
        }else{
            throw new ConflictException("Customer with email "+customer.getEmail()+" already registered");
        }
        if(customer.getAddress()!=null){
            Address persistedAddress = this.addressService.save(customer.getAddress());
            customer.setAddress(persistedAddress);
        }
        customer.setStatus(Status.ACTIVATED);
        return super.save(customer);
    }

    @Override
    public void activateCustomerByToken(String token) {
        Customer customer = this.customerDao.getByVerificationToken(token);
        LOG.debug("Customer fetched by verification token: {}", customer);
        customer.setStatus(ACTIVATED);
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
        if (user.getPassword() == null) {
            user.setPassword(notUpdatedCustomer.getPassword());
        } else {
            user.setPassword(shaPasswordEncoder.encodePassword(user.getPassword(), null));
        }

        if (notUpdatedCustomer.getCorporate() != null) {
            if (user.getCorporate() != null) {
                if (!notUpdatedCustomer.getCorporate().getId().equals(user.getCorporate().getId())) {
                    this.deactivateCustomerTariff(user.getId());
                }
            } else {
                this.deactivateCustomerTariff(user.getId());
            }
        }

        if (notUpdatedCustomer.getAddress() != null) {
            if (user.getAddress() != null) {
                if (!notUpdatedCustomer.getAddress().getRegion().getId().equals(user.getAddress().getRegion().getId())) {
                    this.deactivateCustomerTariff(user.getId());
                }
            } else {
                this.deactivateCustomerTariff(user.getId());
            }
        }

        return super.update(user);
    }

    @Override
    public Map<String, Object> getAllCustomersPaging(int page, int size, long regionId, String status, String partOfEmail,
                                                     String partOfName, String selectedPhone, String partOfCorporate,
                                                     int orderBy, String orderByType) {
        Query query = buildQueryForCustomersTable(page, size, regionId, status, partOfEmail, partOfName, selectedPhone,
                partOfCorporate, orderBy, orderByType);
        Map<String, Object> response = new HashMap<>();
        LOG.debug("Query: {}", query.getQuery());
        response.put("customers", this.customerDao.executeForList(query.getQuery(),
                query.getPreparedStatementParams().toArray()));
        response.put("customersSelected", this.customerDao.executeForInt(query.getCountQuery(),
                query.getCountParams().toArray()));
        return response;
    }

    /**
     * Sends a notification message to all the customers that agreed to receive
     * advertisement emails.
     *
     * @param mailMessage message to be notified about
     */
    @Override
    public void notifyAgreedCustomers(SimpleMailMessage mailMessage) {
        List<Customer> agreedCustomers = this.customerDao.getCustomersAgreedForMailing();
        LOG.debug("Customers agreed for mailing: {}", agreedCustomers);
        this.emailService.sendMail(mailMessage, agreedCustomers);
    }

    private Query buildQueryForCustomersTable(int page, int size, long regionId, String status, String partOfEmail,
                                              String partOfName, String selectedPhone, String partOfCorporate,
                                              int orderBy, String orderByType) {
        Query.Builder builder = new Query.Builder("dbuser inner join address on dbuser.address_id = address.id " +
                "left join corporate on dbuser.corporate_id = corporate.id " +
                "inner join region on address.region_id = region.id");
        builder.where();
        builder.addLikeCondition("dbuser.email", partOfEmail)
                .and().openBracket().addLikeCondition("dbuser.firstname", partOfName)
                .or().addLikeCondition("dbuser.secondname", partOfName)
                .or().addLikeCondition("dbuser.lastname", partOfName).closeBracket()
                .and().addLikeCondition("dbuser.phone", selectedPhone);
        if (partOfCorporate.length() > 0) builder.and().addLikeCondition("corporate.corporate_name", partOfCorporate);
        if (regionId > 0) builder.and().addCondition("address.region_id = ?", regionId);
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
            case 1://by first name
                return "dbuser.firstname";
            case 2://by second name
                return "dbuser.secondname";
            case 3://by last name
                return "dbuser.lastname";
            case 4://by phone
                return "dbuser.phone";
            case 5://by region
                return "region.name_region";
            case 6://by corporate
                return "corporate.corporate_name";
            case 7://by status
                return "dbuser.status";
            default:
                return "";
        }
    }

    @Override
    public void deactivateCustomer(long id) {

    }

    @Override
    public List<Customer> getCustomersByCorporate(long corporateId) {
        return customerDao.getByCorporateId(corporateId);
    }

    @Override
    public List<Customer> getSuitableCustomersForService(long corporateId) {
        return customerDao.getSuitableCustomersForService(corporateId);
    }

    @Override
    public Map<String, Object> getAllCustomersSearch(int page, int size, String email, String phone, String surname, int corporate, int region, String status) {
        Query.Builder queryBuilder = new Query.Builder("dbuser");
        queryBuilder.where();
        queryBuilder.addLikeCondition("email", email);
        queryBuilder.and().addLikeCondition("phone", phone);
        queryBuilder.and().addLikeCondition("lastname", surname);

        if (!status.equals("ALL") && (status.equals("ACTIVATED") || status.equals("DEACTIVATED"))) {
            queryBuilder.and().addCondition("status=?", status);
        } else if (!status.equals("ALL")) {
            throw new ConflictException("Search parameters error: status.");
        }

        if (corporate == -1) {
            queryBuilder.and().addIsNullCondition("corporate_id");
        } else if (corporate > 0) {
            queryBuilder.and().addCondition("corporate_id=?", corporate);
        } else if (corporate < -1) {
            throw new ConflictException("Search parameters error: corporate.");
        }
        queryBuilder.addPaging(page, size);

        Map<String, Object> response = new HashMap<>();

        Query query = queryBuilder.build();
        response.put("customers", customerDao.executeForList(query.getQuery(), query.getPreparedStatementParams().toArray()));
        response.put("entitiesSelected", customerDao.executeForInt(query.getCountQuery(), query.getCountParams().toArray()));
        return response;
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
