package com.phonecompany.controller.advice;

import com.phonecompany.exception.ConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ConflictControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ConflictControllerAdvice.class);

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> conflictException(ConflictException e) {
        LOG.debug("e.getMessage: {}", e.getMessage());
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.CONFLICT);
    }
}
