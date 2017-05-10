package com.phonecompany.controller;

import com.phonecompany.model.Customer;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.MailMessageCreator;
import com.phonecompany.service.interfaces.TariffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/tariff-region")
public class TariffRegionController {

    private static final Logger LOG = LoggerFactory.getLogger(TariffRegionController.class);

    private TariffService tariffService;
    private CustomerService customerService;
    private EmailService<Customer> emailService;
    private MailMessageCreator<Tariff> tariffNotificationMailCreator;

    @Autowired
    public TariffRegionController(TariffService tariffService,
                                  CustomerService customerService,
                                  EmailService<Customer> emailService,
                                  @Qualifier("tariffNotificationEmailCreator")
                                  MailMessageCreator<Tariff> tariffNotificationMailCreator){
        this.tariffService = tariffService;
        this.customerService = customerService;
        this.emailService = emailService;
        this.tariffNotificationMailCreator = tariffNotificationMailCreator;
    }

    @PostMapping
    public ResponseEntity<?> saveTariffRegion(@RequestBody List<TariffRegion> tariffRegions) {
        Tariff savedTariff = tariffService.addNewTariff(tariffRegions);
        SimpleMailMessage notificationMessage = this.tariffNotificationMailCreator
                .constructMessage(savedTariff);
        this.notifyAgreedCustomers(notificationMessage);
        return new ResponseEntity<Object>(savedTariff, HttpStatus.CREATED);
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

    @PutMapping
    public ResponseEntity<?> updateTariff(@RequestBody List<TariffRegion> tariffRegions) {
        Tariff updatedTariff = tariffService.updateTariff(tariffRegions);
        return new ResponseEntity<Object>(updatedTariff, HttpStatus.OK);
    }
}
