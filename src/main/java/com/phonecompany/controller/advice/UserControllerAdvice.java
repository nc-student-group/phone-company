package com.phonecompany.controller.advice;

import com.phonecompany.exception.service_layer.KeyAlreadyPresentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(UserControllerAdvice.class);

    @ExceptionHandler(KeyAlreadyPresentException.class)
    public ResponseEntity<?> emailAlreadyPresentException(KeyAlreadyPresentException e) {
        LOG.debug("e.getMessage: {}", e.getMessage());
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.CONFLICT);
    }
}
