package com.phonecompany.controller;

import com.phonecompany.model.Service;
import com.phonecompany.service.interfaces.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "api/services")
public class ServicesController {

    private static final Logger LOG = LoggerFactory.getLogger(ServicesController.class);
    private ServiceService serviceService;

    @Autowired
    public ServicesController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping(value = "/category/{id}/{page}/{size}")
    public Map<String, Object> getServicesByCategoryId(@PathVariable("id") Long productCategoryId,
                                                       @PathVariable("page") int page,
                                                       @PathVariable("size") int size) {
        LOG.debug("Fetching services for the product category with an id: {}", productCategoryId);
        return serviceService.getServicesByProductCategoryId(productCategoryId, page, size);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> addService(@RequestBody Service service) {
        LOG.debug("Service parsed from the request body: {}", service);
        Service persistedService = this.serviceService.validateAndSave(service);
        return new ResponseEntity<>(persistedService, HttpStatus.OK);
    }

    @GetMapping(value = "/new")
    public Service getEmptyService() {
        return new Service();
    }
}
