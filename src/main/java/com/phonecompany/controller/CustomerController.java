package com.phonecompany.controller;

import com.phonecompany.annotations.ValidateParams;
import com.phonecompany.model.Customer;
import com.phonecompany.model.User;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.model.enums.Status;
import com.phonecompany.service.email.customer_related_emails.ConfirmationEmailCreator;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    @Value("${registration-success-url}")
    private String registrationSuccessUrl;

    private CustomerService customerService;
    private AddressService addressService;
    private UserService userService;
    private VerificationTokenService verificationTokenService;
    private ConfirmationEmailCreator confirmMessageCreator;
    private EmailService<Customer> emailService;

    @Autowired
    public CustomerController(CustomerService customerService,
                              AddressService addressService,
                              UserService userService,
                              VerificationTokenService verificationTokenService,
                              ConfirmationEmailCreator confirmMessageCreator,
                              EmailService<Customer> emailService) {
        this.customerService = customerService;
        this.addressService = addressService;
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.confirmMessageCreator = confirmMessageCreator;
        this.emailService = emailService;
    }

    @ValidateParams
    @PostMapping("/api/customers")
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        LOG.debug("Customer retrieved from the http request: {}", customer);
        Customer persistedCustomer = this.customerService.save(customer);
        VerificationToken persistedToken = this.verificationTokenService
                .saveTokenForUser(persistedCustomer);
        SimpleMailMessage confirmationMessage =
                this.confirmMessageCreator.constructMessage(persistedToken);
        LOG.info("Sending email confirmation message to: {}", persistedCustomer.getEmail());
        emailService.sendMail(confirmationMessage, persistedCustomer);
        return new ResponseEntity<>(persistedCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/api/customers/empty-customer")
    public Customer getEmptyCustomer() {
        return new Customer();
    }

    @GetMapping("/api/customers/{page}/{size}/{rId}/{status}")
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
        URI registration = new URI(registrationSuccessUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(registration);
        return httpHeaders;
    }

    @PostMapping("/api/customer/save")
    public ResponseEntity<?> saveCustomerByAdmin(@RequestBody Customer customer) {
        return new ResponseEntity<>(this.customerService.saveByAdmin(customer), HttpStatus.CREATED);
    }

    @GetMapping("/api/customers/logged-in-user")
    public Customer getCustomerByCurrentUserId() {
        User loggedInUser = this.userService.getCurrentlyLoggedInUser();
        LOG.debug("Currently logged in user retrieved from the security context: {}", loggedInUser);
        Customer loggedInCustomer = customerService.getById(loggedInUser.getId());
        LOG.debug("Currently logged in customer: {}", loggedInCustomer);
        return loggedInCustomer;
    }

    @GetMapping("/api/customer/getByCorporateId/{id}")
    public List<Customer> getCustomerByCorporateId(@PathVariable("id") long corporateId) {
        return this.customerService.getCustomersByCorporate(corporateId);
    }

    @GetMapping("/api/customers/suitableCustomersForService/{corporateId}")
    public List<Customer> getSuitableCustomersForService(@PathVariable("corporateId") long corporateId) {
        return this.customerService.getSuitableCustomersForService(corporateId);
    }

    @PatchMapping("/api/customers/")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        LOG.debug("Customer retrieved from the http request: {}", customer);

        this.customerService.update(customer);
        if (customer.getAddress() != null) {
            this.addressService.update(customer.getAddress());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/api/customers/status/update/{id}/{status}")
    public ResponseEntity<Void> updateUserStatus(@PathVariable("id") long id, @PathVariable("status") Status status) {
        if (status.equals(Status.DEACTIVATED)) {
            customerService.deactivateCustomerTariff(id);
        }
        customerService.updateStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/customers/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") long id) {
        return new ResponseEntity<Object>(customerService.getById(id), HttpStatus.OK);
    }

    @PutMapping("/api/customers/{id}/mailingAgreement/{agreementState}")
    public ResponseEntity<?> changeAgreement(@PathVariable("id") long customerId,
                                             @PathVariable("agreementState") boolean state) {
        this.customerService.changeMailingAgreement(state, customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
