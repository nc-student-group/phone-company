package com.phonecompany.controller;

import com.phonecompany.model.Customer;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.model.Service;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.email.AbstractEmailCreator;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/services")
public class ServicesController {

    private static final Logger LOG = LoggerFactory.getLogger(ServicesController.class);

    private ServiceService serviceService;
    private ProductCategoryService productCategoryService;
    private MailMessageCreator<Service> emailCreator;
    private EmailService<Customer> emailService;
    private CustomerService customerService;

    @Autowired
    public ServicesController(ServiceService serviceService,
                              ProductCategoryService productCategoryService,
                              @Qualifier("serviceNotificationEmailCreator")
                                      MailMessageCreator<Service> emailCreator,
                              EmailService<Customer> emailService,
                              CustomerService customerService) {
        this.serviceService = serviceService;
        this.productCategoryService = productCategoryService;
        this.emailCreator = emailCreator;
        this.emailService = emailService;
        this.customerService = customerService;
    }

    @GetMapping("/category/{id}/{page}/{size}")
    public Map<String, Object> getServicesByCategoryId(@PathVariable("id") Long productCategoryId,
                                                       @PathVariable("page") int page,
                                                       @PathVariable("size") int size) {
        LOG.debug("Fetching services for the product category with an id: {}", productCategoryId);
        return serviceService.getServicesByProductCategoryId(productCategoryId, page, size);
    }

    @PostMapping
    public ResponseEntity<?> addService(@RequestBody Service service) {
        LOG.debug("Service parsed from the request body: {}", service);
        Service persistedService = this.serviceService.save(service);
        SimpleMailMessage mailMessage = this.emailCreator.constructMessage(service);
        this.notifyAllCustomers(mailMessage);
        return new ResponseEntity<>(persistedService, HttpStatus.OK);
    }

    private void notifyAllCustomers(SimpleMailMessage mailMessage) {
        List<Customer> allCustomers = this.customerService.getAll();
        this.emailService.sendMail(mailMessage, allCustomers);
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

    @GetMapping("/new")
    public Service getEmptyService() {
        return new Service();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateServiceStatus(@PathVariable("id") long id,
                                                 @RequestBody String status) { //TODO: must be enum
        LOG.debug("Service id to update: {}, status: {}", id, status);
        this.serviceService.updateServiceStatus(id, ProductStatus.valueOf(status));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> updateService(@RequestBody Service service) {
        LOG.debug("Service to be updated", service);
        this.serviceService.update(service);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
