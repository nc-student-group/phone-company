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

import java.util.HashMap;

@ControllerAdvice
class UserControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(UserControllerAdvice.class);

    @ExceptionHandler(EmailAlreadyPresentException.class)
    public ResponseEntity<?> emailAlreadyPresentException(EmailAlreadyPresentException e) {
        LOG.debug("e.getMessage: {}", e.getMessage());
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.CONFLICT);
    }

    //TODO: replace with new Error()
    private static final class Response {

        private String message;

        public Response() {
        }

        public Response(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
