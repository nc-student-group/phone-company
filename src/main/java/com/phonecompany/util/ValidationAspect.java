package com.phonecompany.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.*;
import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
public class ValidationAspect {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationAspect.class);

    private ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
    private Validator validator = vf.getValidator();

    @Around("@annotation(com.phonecompany.annotations.ValidateParams)")
    public Object validateParameters(ProceedingJoinPoint joinPoint) throws Throwable {

        LOG.debug("Validating arguments for method: {}", joinPoint.getTarget());

        Set<ConstraintViolation<Object>> constraintViolations =
                this.collectParamViolations(joinPoint.getArgs());

        LOG.debug("There were {} constraint violations among method parameters",
                constraintViolations.size());

        if(!constraintViolations.isEmpty()) {
            String constraintViolationsMessage = this.getViolationsMessage(constraintViolations);
            throw new ValidationException(constraintViolationsMessage);
        }

        return joinPoint.proceed();
    }

    private Set<ConstraintViolation<Object>> collectParamViolations(Object[] methodParams) {
        HashSet<ConstraintViolation<Object>> constraintViolations = new HashSet<>();
        for (Object methodParam : methodParams) {
            constraintViolations.addAll(this.getViolationsFromParam(methodParam));
        }
        return constraintViolations;
    }

    private Set<ConstraintViolation<Object>> getViolationsFromParam(Object methodArg) {
        LOG.debug("Validating argument: {}", methodArg);
        return validator.validate(methodArg);
    }

    private String getViolationsMessage(Set<ConstraintViolation<Object>> constraintViolations) {
        StringBuilder violationMessageBuilder = new StringBuilder();
        for (ConstraintViolation<Object> violation : constraintViolations) {
            violationMessageBuilder.append(this.stringifyViolation(violation));
        }
        return violationMessageBuilder.toString();
    }

    private String stringifyViolation(ConstraintViolation<Object> violation) {
        return String.format("property: [%s], value: [%s], message: [%s] \n",
                violation.getPropertyPath(), violation.getInvalidValue(), violation.getMessage());
    }
}
