package com.phonecompany.controller.advice;

import com.phonecompany.exception.ConflictException;
import com.phonecompany.exception.EmptyResultSetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @ExceptionHandler(EmptyResultSetException.class)
    public ResponseEntity<?> emptyResultSetException(EmptyResultSetException e) {
        LOG.debug("e.getMessage: {}", e.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.BAD_REQUEST);
    }
}
