package com.phonecompany.controller.advice;

import com.phonecompany.exception.ConflictException;
import com.phonecompany.exception.service_layer.MissingResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Intercepts exceptions in order to produce an object containing
 * information regarding the error.
 */
@ControllerAdvice
public class UniversalControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(UniversalControllerAdvice.class);

    /**
     * Responds with an object that exposes to a REST consumer a respective
     * {@code HttpStatus} code as well as an error body that enables to take
     * a corresponding action in return
     *
     * <p>Gets invoked if an exception of type {@link ConflictException}
     * takes place.</p>
     *
     * @param e exception object containing information regarding the error
     * @return object containing {@code HttpStatus} and a corresponding message
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> conflictException(ConflictException e) {
        LOG.debug("e.getMessage: {}", e.getMessage());
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.CONFLICT);
    }

    /**
     * Responds with an object that exposes to a REST consumer a respective
     * {@code HttpStatus} code as well as an error body that enables to take
     * a corresponding action in return
     *
     * <p>Gets invoked if an exception of type {@link MissingResultException}
     * takes place.</p>
     *
     * @param e exception object containing information regarding the error
     * @return object containing {@code HttpStatus} and a corresponding message
     */
    @ExceptionHandler(MissingResultException.class)
    public ResponseEntity<?> emptyResultSetException(MissingResultException e) {
        LOG.debug("e.getMessage: {}", e.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.BAD_REQUEST);
    }
}
