package com.phonecompany.controller;

import com.phonecompany.model.*;
import com.phonecompany.model.enums.Status;
import com.phonecompany.service.email.customer_related_emails.ConfirmationEmailCreator;
import com.phonecompany.service.email.customer_related_emails.PasswordAssignmentEmailCreator;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    @Value("${application-url}")
    private String applicationUrl;

    private CustomerService customerService;
    private AddressService addressService;
    private UserService userService;
    private VerificationTokenService verificationTokenService;
    private ConfirmationEmailCreator confirmMessageCreator;
    private PasswordAssignmentEmailCreator passwordAssignmentEmailCreator;
    private EmailService<Customer> emailService;

    @Autowired
    public CustomerController(CustomerService customerService,
                              AddressService addressService,
                              UserService userService,
                              VerificationTokenService verificationTokenService,
                              ConfirmationEmailCreator confirmMessageCreator,
                              PasswordAssignmentEmailCreator passwordAssignmentEmailCreator,
                              EmailService<Customer> emailService) {
        this.customerService = customerService;
        this.addressService = addressService;
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.confirmMessageCreator = confirmMessageCreator;
        this.passwordAssignmentEmailCreator = passwordAssignmentEmailCreator;
        this.emailService = emailService;
    }

    @RequestMapping(method = POST, value = "/api/customers")
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        LOG.debug("Customer retrieved from the http request: " + customer);
        Customer persistedCustomer = this.customerService.save(customer);
        LOG.debug("Customer persisted with an id: " + persistedCustomer.getId());
        VerificationToken persistedToken = this.verificationTokenService
                .saveTokenForUser(persistedCustomer);
        SimpleMailMessage confirmationMessage =
                this.confirmMessageCreator.constructMessage(persistedToken);
        LOG.info("Sending email confirmation message to: {}", persistedCustomer.getEmail());
        emailService.sendMail(confirmationMessage, persistedCustomer);
        return new ResponseEntity<>(persistedCustomer, HttpStatus.CREATED);
    }

    @GetMapping(value = "/api/customers/empty-customer")
    public Customer getEmptyCustomer() {
        return new Customer();
    }

    @RequestMapping(method = GET, value = "/api/customers/{page}/{size}/{rId}/{status}")
    public Map<String, Object> getAllCustomers(@PathVariable("page") int page,
                                               @PathVariable("size") int size,
                                               @PathVariable("rId") long rId,
                                               @PathVariable("status") String status,
                                               @RequestParam("em") String email,
                                               @RequestParam("pon") String partOfName,
                                               @RequestParam("ph") String phone,
                                               @RequestParam("poc") String partOfCorporate,
                                               @RequestParam("ob") int orderBy,
                                               @RequestParam("obt") String orderByType) {
        LOG.info("Retrieving all the users contained in the database");
        return this.customerService.getAllCustomersPaging(page, size, rId, status, email, partOfName,
                phone, partOfCorporate, orderBy, orderByType);
    }

    @GetMapping("/api/verifyRegistration")
    public ResponseEntity<? extends User> confirmRegistration(@RequestParam String token)
            throws URISyntaxException {
        LOG.debug("Token retrieved from the request parameter: {}", token);
        this.customerService.activateCustomerByToken(token);
        HttpHeaders redirectionHeaders = this.getRedirectionHeaders();

        return new ResponseEntity<>(redirectionHeaders, HttpStatus.SEE_OTHER);
    }

    private HttpHeaders getRedirectionHeaders() throws URISyntaxException {
        URI registration = new URI("https://phone-company.herokuapp.com/#/login/success");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(registration);
        return httpHeaders;
    }

    @RequestMapping(method = POST, value = "/api/customer/save")
    public ResponseEntity<?> saveCustomerByAdmin(@RequestBody Customer customer) {
        return new ResponseEntity<>(this.customerService.saveByAdmin(customer), HttpStatus.CREATED);
    }

    @RequestMapping(method = GET, value = "/api/customers/logged-in-user")
    public Customer getCustomerByCurrentUserId() {
        User loggedInUser = this.userService.getCurrentlyLoggedInUser();
        LOG.debug("Currently logged in user retrieved from the security context: {}", loggedInUser);
        Customer loggedInCustomer = customerService.getById(loggedInUser.getId());
        LOG.debug("Currently logged in customer: {}", loggedInCustomer);
        return loggedInCustomer;
    }

    @RequestMapping(method = GET, value = "/api/customer/getByCorporateId/{id}")
    public List<Customer> getCustomerByCorporateId(@PathVariable("id") long corporateId) {
        return this.customerService.getCustomersByCorporate(corporateId);
    }

    @RequestMapping(method = GET, value = "/api/customers/suitableCustomersForService/{corporateId}")
    public List<Customer> getSuitableCustomersForService(@PathVariable("corporateId") long corporateId) {
        return this.customerService.getSuitableCustomersForService(corporateId);
    }

    @PatchMapping(value = "/api/customers/")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        LOG.debug("Customer retrieved from the http request: {}", customer);

        this.customerService.update(customer);
        if(customer.getAddress()!=null){
            this.addressService.update(customer.getAddress());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/customers/status/update/{id}/{status}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateUserStatus(@PathVariable("id") long id, @PathVariable("status") Status status) {
        if (status.equals(Status.DEACTIVATED)) {
            customerService.deactivateCustomerTariff(id);
        }
        customerService.updateStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/api/customers/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") long id) {
        return new ResponseEntity<Object>(customerService.getById(id), HttpStatus.OK);
    }
}
