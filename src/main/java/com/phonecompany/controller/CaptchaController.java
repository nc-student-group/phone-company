package com.phonecompany.controller;

import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.CaptchaService;
import com.phonecompany.service.interfaces.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    private CaptchaService captchaService;
    private TariffService tariffService;

    @Autowired
    public CaptchaController(CaptchaService captchaService, TariffService tariffService) {
        this.captchaService = captchaService;
        this.tariffService = tariffService;
    }

    @PostMapping
    public ResponseEntity<Void> checkCaptcha(@RequestBody String captchaResponse) {
        if (captchaService.verify(captchaResponse))
            return new ResponseEntity<Void>(HttpStatus.OK);
        else return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    @GetMapping(value = "/test")
    public ResponseEntity<?> testTransaction() {
        Tariff t = new Tariff("qwweqweqw", ProductStatus.ACTIVATED, "111", "111", "111", "111","111","111", false, LocalDate.now(), 0.0, "", 0.0);
        tariffService.addNewTariff(t);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
