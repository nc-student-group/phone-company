package com.phonecompany.controller;

import com.phonecompany.model.*;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.*;
import com.phonecompany.service.interfaces.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/services")
public class ServicesController {

    private static final Logger LOG = LoggerFactory.getLogger(ServicesController.class);

    private ServiceService serviceService;
    private ProductCategoryService productCategoryService;
    private MailMessageCreator<Service> serviceNotificationEmailCreator;
    private EmailService<Customer> emailService;
    private CustomerService customerService;
    private MailMessageCreator<Service> serviceActivationNotificationEmailCreator;
    private CustomerServiceService customerServiceService;
    private OrderService orderService;

    @Autowired
    public ServicesController(ServiceService serviceService,
                              ProductCategoryService productCategoryService,
                              @Qualifier("serviceNotificationEmailCreator")
                                      MailMessageCreator<Service> emailCreator,
                              EmailService<Customer> emailService,
                              CustomerService customerService,
                              CustomerServiceService customerServiceService,
                              OrderService orderService,
                              @Qualifier("serviceActivationNotificationEmailCreator")
                                      MailMessageCreator<Service> serviceActivationNotificationEmailCreator) {
        this.serviceService = serviceService;
        this.productCategoryService = productCategoryService;
        this.serviceNotificationEmailCreator = emailCreator;
        this.emailService = emailService;
        this.customerService = customerService;
        this.customerServiceService = customerServiceService;
        this.orderService = orderService;
        this.serviceActivationNotificationEmailCreator = serviceActivationNotificationEmailCreator;
    }

    @GetMapping
    public Collection<Service> getAllServices() {
        List<Service> allServices = this.serviceService.getAll();
        LOG.debug("Services fetched from the storage: {}", allServices);
        return allServices;
    }

    @GetMapping("/category/{id}/{page}/{size}")
    public Map<String, Object> getServicesByCategoryId(@PathVariable("id") long productCategoryId,
                                                       @PathVariable("page") int page,
                                                       @PathVariable("size") int size) {
        LOG.debug("Fetching services for the product category with an id: {}", productCategoryId);
        return serviceService
                .getServicesByProductCategoryId(productCategoryId, page, size);
    }

    @PostMapping
    public ResponseEntity<?> addService(@RequestBody Service service) {
        LOG.debug("Service parsed from the request body: {}", service);
        Service persistedService = this.serviceService.save(service);
        SimpleMailMessage mailMessage = this
                .serviceNotificationEmailCreator.constructMessage(service);
        this.notifyAgreedCustomers(mailMessage);
        return new ResponseEntity<>(persistedService, HttpStatus.OK);
    }

    private void notifyAgreedCustomers(SimpleMailMessage mailMessage) {
        List<Customer> agreedCustomers = this.getAgreedCustomers();
        LOG.debug("Customers agreed for mailing: {}", agreedCustomers);
        this.emailService.sendMail(mailMessage, agreedCustomers);
    }

    private List<Customer> getAgreedCustomers() {
        return this.customerService.getAll().stream()
                .filter(Customer::getIsMailingEnabled)
                .collect(Collectors.toList());
    }

    @GetMapping("/activate/{serviceId}")
    public ResponseEntity<?> activateServiceForUser(@PathVariable("serviceId") long serviceId) {
        Customer loggedInCustomer = this.customerService.getCurrentlyLoggedInUser();
        Service currentService = this.serviceService.getById(serviceId);
        this.serviceService.activateServiceForCustomer(currentService, loggedInCustomer);
        SimpleMailMessage notificationMessage = this
                .serviceActivationNotificationEmailCreator.constructMessage(currentService);
        this.emailService.sendMail(notificationMessage, loggedInCustomer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/categories")
    public List<ProductCategory> getAllCategories() {
        List<ProductCategory> productCategoryList = this.productCategoryService.getAll();
        LOG.debug("All the product categories: {}", productCategoryList);
        return productCategoryList;
    }

    @GetMapping("/{id}")
    public Service getServiceById(@PathVariable("id") long serviceId) {
        Service serviceFetchedById = this.serviceService.getById(serviceId);
        LOG.debug("Service fetched by id: {}", serviceFetchedById);
        return serviceFetchedById;
    }

    @GetMapping("/empty-service")
    public Service getEmptyService() {
        return new Service();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateServiceStatus(@PathVariable("id") long serviceId,
                                                 @RequestBody String status) { //TODO: must be enum
        LOG.debug("Service id to update: {}, status: {}", serviceId, status);
        this.serviceService.updateServiceStatus(serviceId, ProductStatus.valueOf(status));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> updateService(@RequestBody Service service) {
        LOG.debug("Service to be updated: ", service);
        this.serviceService.update(service);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentActiveOrSuspendedCustomerTariff() {
        Customer customer = customerService.getCurrentlyLoggedInUser();
        List<CustomerServiceDto> customerServices =
                customerServiceService.getCurrentCustomerServices(customer.getId());
        return new ResponseEntity<Object>(customerServices, HttpStatus.OK);
    }

    @GetMapping("/current/customer/{id}")
    public ResponseEntity<?> getCurrentActiveOrSuspendedCustomerTariff(@PathVariable("id") long customerId) {
        Customer customer = customerService.getById(customerId);
        List<CustomerServiceDto> customerServices =
                customerServiceService.getCurrentCustomerServices(customer.getId());
        return new ResponseEntity<Object>(customerServices, HttpStatus.OK);
    }

    @GetMapping("/history/{page}/{size}")
    public Map<String, Object> getOrdersHistoryPaged(@PathVariable("page") int page,
                                                     @PathVariable("size") int size) {
        Customer customer = customerService.getCurrentlyLoggedInUser();
        LOG.debug("Get all service orders by customer id = " + customer);
        Map<String, Object> map = new HashMap<>();
        map.put("ordersFound", orderService.getOrdersCountForServicesByClient(customer));
        map.put("orders", orderService.getOrdersHistoryForServicesByClient(customer, page, size));
        return map;
    }

    @GetMapping("/history/customer/{id}/{page}/{size}")
    public Map<String, Object> getOrdersHistoryPaged(@PathVariable("id") long id,
                                                     @PathVariable("page") int page,
                                                     @PathVariable("size") int size) {
        Customer customer = customerService.getById(id);
        LOG.debug("Get all service orders by customer id = " + customer);
        Map<String, Object> map = new HashMap<>();
        map.put("ordersFound", orderService.getOrdersCountForServicesByClient(customer));
        map.put("orders", orderService.getOrdersHistoryForServicesByClient(customer, page, size));
        return map;
    }

    @PatchMapping(value = "/deactivate")
    public ResponseEntity<Void> deactivateCustomerService(@RequestBody CustomerServiceDto customerService) {
        customerServiceService.deactivateCustomerService(customerService);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/activate")
    public ResponseEntity<Void> activateCustomerService(@RequestBody CustomerServiceDto customerService) {
        customerServiceService.activateCustomerService(customerService);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/suspend")
    public ResponseEntity<Void> suspendCustomerService(@RequestBody Map<String, Object> data) {
        customerServiceService.suspendCustomerService(data);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
