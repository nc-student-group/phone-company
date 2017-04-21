package com.phonecompany.controller.advice;

import com.phonecompany.exception.EmailAlreadyPresentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
class UserControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(UserControllerAdvice.class);
    private final MediaType vndErrorMediaType = MediaType.parseMediaType("application/vnd.error");

    @ExceptionHandler(EmailAlreadyPresentException.class)
    ResponseEntity<?> emailAlreadyPresentException(EmailAlreadyPresentException e) {
        LOG.debug("e.getMessage: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
    }
}
