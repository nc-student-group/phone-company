package com.phonecompany.controller;

import com.phonecompany.service.interfaces.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    private CaptchaService captchaService;

    @Autowired
    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @PostMapping
    public ResponseEntity<Void> checkCaptcha(@RequestBody String captchaResponse) {
        if (captchaService.verify(captchaResponse))
            return new ResponseEntity<Void>(HttpStatus.OK);
        else return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
}
