package com.phonecompany.controller.advice;

import com.phonecompany.exception.ServiceAlreadyPresentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServiceControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceControllerAdvice.class);

    /**
     * Intercepts an exception in order to produce an object containing
     * information regarding the error.
     * Gets fired up if an exception of type {@code ServiceAlreadyPresentException}
     * takes place
     *
     * @param e exception object containing information about the error
     * @return  an object that exposes to a REST consumer a respective
     *          {@code HttpStatus} code as well as an error body that
     *          enables to take a corresponding action in return
     */
    @ExceptionHandler(ServiceAlreadyPresentException.class)
    public ResponseEntity<?> serviceAlreadyPresentException(ServiceAlreadyPresentException e) {
        LOG.debug("e.getMessage: {}", e.getMessage());
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.CONFLICT);
    }
}