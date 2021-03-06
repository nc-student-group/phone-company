package com.phonecompany.controller;

import com.phonecompany.annotations.ValidateParams;
import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.model.paging.PagingResult;
import com.phonecompany.service.email.service_related_emails.ServiceActivationNotificationEmailCreator;
import com.phonecompany.service.email.service_related_emails.ServiceDeactivationNotificationEmailCreator;
import com.phonecompany.service.email.service_related_emails.ServiceNotificationEmailCreator;
import com.phonecompany.service.email.service_related_emails.ServiceSuspensionNotificationEmailCreator;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/services")
public class ServicesController {

    private static final Logger LOG = LoggerFactory.getLogger(ServicesController.class);

    private ServiceService serviceService;
    private ProductCategoryService productCategoryService;
    private CustomerService customerService;
    private EmailService<Customer> emailService;
    private ServiceNotificationEmailCreator serviceNotificationEmailCreator;
    private ServiceActivationNotificationEmailCreator serviceActivationNotificationEmailCreator;
    private ServiceDeactivationNotificationEmailCreator serviceDeactivationNotificationEmailCreator;
    private ServiceSuspensionNotificationEmailCreator serviceSuspensionNotificationEmailCreator;
    private CustomerServiceService customerServiceService;
    private OrderService orderService;

    @Autowired
    public ServicesController(ServiceService serviceService,
                              ProductCategoryService productCategoryService,
                              EmailService<Customer> emailService,
                              ServiceNotificationEmailCreator serviceNotificationEmailCreator,
                              ServiceActivationNotificationEmailCreator serviceActivationNotificationEmailCreator,
                              ServiceDeactivationNotificationEmailCreator serviceDeactivationNotificationEmailCreator,
                              ServiceSuspensionNotificationEmailCreator serviceSuspensionNotificationEmailCreator,
                              CustomerService customerService,
                              CustomerServiceService customerServiceService,
                              OrderService orderService) {
        this.serviceService = serviceService;
        this.productCategoryService = productCategoryService;
        this.emailService = emailService;
        this.serviceNotificationEmailCreator = serviceNotificationEmailCreator;
        this.serviceActivationNotificationEmailCreator = serviceActivationNotificationEmailCreator;
        this.serviceDeactivationNotificationEmailCreator = serviceDeactivationNotificationEmailCreator;
        this.serviceSuspensionNotificationEmailCreator = serviceSuspensionNotificationEmailCreator;
        this.customerService = customerService;
        this.customerServiceService = customerServiceService;
        this.orderService = orderService;
    }

    @GetMapping
    public Collection<Service> getAllServices() {
        LOG.debug("Fetching all the services...");
        return this.serviceService.getAll();
    }

    @GetMapping(value = "/active")
    public Collection<Service> getAllActiveServices() {
        LOG.debug("Fetching all the active services...");
        List<Service> activeServices = this.serviceService.getServicesByStatus(ProductStatus.ACTIVATED);
        LOG.debug("Services for client tab");
        return this.serviceService.applyDiscount(activeServices);
    }

    @GetMapping(value = "/activeWithDiscount")
    public Collection<Service> getAllActiveServicesWithDiscount() {
        LOG.debug("Fetching all the active services with discount...");
        return this.serviceService.getAllActiveServicesWithDiscount();
    }

    @GetMapping(value = "/top")
    public Collection<Service> getTopActiveServices() {
        LOG.debug("Fetching top among the active services...");
        return this.serviceService.getTopActiveServices();
    }

    @GetMapping("/category/{id}/{page}/{size}")
    public ResponseEntity<?> getServicesByCategoryId(@PathVariable("id") int productCategoryId,
                                                     @PathVariable("page") int page,
                                                     @PathVariable("size") int size,
                                                     @RequestParam("partOfName") String partOfName,
                                                     @RequestParam("startingPrice") double startingPrice,
                                                     @RequestParam("endingPrice") double endingPrice,
                                                     @RequestParam("selectedStatus") int status,
                                                     @RequestParam("orderingCategory") int orderingCategory,
                                                     @RequestParam("orderType") String orderType) {
        LOG.debug("Fetching services for the product category with an id: {}", productCategoryId);
        PagingResult<Service> servicePagingResult = serviceService
                .getServicesByProductCategoryId(page, size, productCategoryId, partOfName,
                        startingPrice, endingPrice, status, orderingCategory, orderType);
        return new ResponseEntity<>(servicePagingResult, HttpStatus.OK);
    }

    @ValidateParams
    @PostMapping
    public ResponseEntity<?> addService(@RequestBody Service service) {
        LOG.debug("Service parsed from the request body: {}", service);
        Service persistedService = this.serviceService.save(service);
        SimpleMailMessage mailMessage = this
                .serviceNotificationEmailCreator.constructMessage(service);
        this.customerService.notifyAgreedCustomers(mailMessage);
        return new ResponseEntity<>(persistedService, HttpStatus.OK);
    }

    @GetMapping("/activate/{serviceId}")
    public ResponseEntity<?> activateServiceForUser(@PathVariable("serviceId") long serviceId) {
        Customer loggedInCustomer = this.customerService.getCurrentlyLoggedInUser();
        CustomerServiceDto activatedCustomerService = this.customerServiceService
                .activateServiceForCustomer(serviceId, loggedInCustomer, false);
        this.orderService.saveCustomerServiceOrder(activatedCustomerService, OrderType.ACTIVATION);
        SimpleMailMessage notificationMessage = this
                .serviceActivationNotificationEmailCreator
                .constructMessage(activatedCustomerService.getService());
        this.emailService.sendMail(notificationMessage, loggedInCustomer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/activate/{serviceId}/{customerId}")
    public ResponseEntity<?> activateServiceForUser(@PathVariable("serviceId") long serviceId,
                                                    @PathVariable("customerId") long customerId) {
        Customer customer = this.customerService.getById(customerId);
        CustomerServiceDto activatedCustomerService = this.customerServiceService
                .activateServiceForCustomer(serviceId, customer, true);
        this.orderService.saveCustomerServiceOrder(activatedCustomerService, OrderType.ACTIVATION);
        SimpleMailMessage notificationMessage = this
                .serviceActivationNotificationEmailCreator
                .constructMessage(activatedCustomerService.getService());
        this.emailService.sendMail(notificationMessage, customer);
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
                                                 @RequestBody String status) {
        LOG.debug("Service id to update: {}, status: {}", serviceId, status);
        this.serviceService.updateServiceStatus(serviceId, ProductStatus.valueOf(status));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ValidateParams
    @PatchMapping
    public ResponseEntity<?> updateService(@RequestBody Service service) {
        LOG.debug("Service to be updated: ", service);
        Service updatedService = this.serviceService.update(service);
        LOG.debug("Updated service: {}", updatedService);
        return new ResponseEntity<>(updatedService, HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentActiveOrSuspendedCustomerTariff() {
        Customer customer = customerService.getCurrentlyLoggedInUser();
        List<CustomerServiceDto> customerServices =
                customerServiceService.getCurrentCustomerServices(customer.getId());
        return new ResponseEntity<Object>(customerServices, HttpStatus.OK);
    }

    @GetMapping("/current/customer/{id}")
    public ResponseEntity<?> getCurrentActiveOrSuspendedCustomerServices(@PathVariable("id") long customerId) {
        Customer customer = customerService.getById(customerId);
        List<CustomerServiceDto> customerServices =
                customerServiceService.getCurrentCustomerServices(customer.getId());
        return new ResponseEntity<Object>(customerServices, HttpStatus.OK);
    }

    @GetMapping("/history/{page}/{size}")
    public Map<String, Object> getOrdersHistoryPaged(@PathVariable("page") int page,
                                                     @PathVariable("size") int size) {
        Customer customer = customerService.getCurrentlyLoggedInUser();
        LOG.debug("Get all service orders by customer id: {}", customer.getId());
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
        SimpleMailMessage notificationMessage = this.serviceDeactivationNotificationEmailCreator
                .constructMessage(customerService.getService());
        this.emailService.sendMail(notificationMessage, customerService.getCustomer());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/resume")
    public ResponseEntity<Void> resumeCustomerService(@RequestBody CustomerServiceDto customerService) {
        customerServiceService.resumeCustomerService(customerService);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/suspend")
    public ResponseEntity<Void> suspendCustomerService(@RequestBody Map<String, Object> data) {
        customerServiceService.suspendCustomerService(data);
        //TODO: this questionable line appears to be unavoidable
        CustomerServiceDto customerService = this.customerServiceService.
                getById((new Long((Integer) data.get("customerServiceId"))));
        SimpleMailMessage notificationMessage = this.serviceSuspensionNotificationEmailCreator
                .constructMessage(customerService.getService());
        this.emailService.sendMail(notificationMessage, customerService.getCustomer());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/productCategoryAvailable/{customerId}/{categoryId}/{isForCorporateCustomer}")
    public Boolean isProductCategoryAvailable(@PathVariable("customerId") long customerId,
                                              @PathVariable("categoryId") long categoryId,
                                              @PathVariable("isForCorporateCustomer") boolean isForCorporateCustomer) {
        Customer customer = this.customerService.getById(customerId);
        return customerServiceService.isProductCategoryAvailable(customer, categoryId, isForCorporateCustomer);
    }
}
