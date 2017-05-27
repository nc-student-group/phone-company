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

    private final Logger LOG = LoggerFactory.getLogger(ValidationAspect.class);
    private ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
    private Validator validator = vf.getValidator();

    @Around("@annotation(com.phonecompany.annotations.Validate)")
    public Object validateParameters(ProceedingJoinPoint joinPoint)
            throws Throwable {

        LOG.debug("Validating arguments for method: {}", joinPoint.getTarget());
        HashSet<ConstraintViolation<Object>> constraintViolations = new HashSet<>();

        for (Object methodParam : joinPoint.getArgs()) {
            constraintViolations.addAll(this.getViolationsFromParam(methodParam));
        }

        StringBuilder violationMessageBuilder = new StringBuilder();

        for (ConstraintViolation<Object> cv : constraintViolations) {
            violationMessageBuilder.append(this.getViolationMessage(cv));
        }
        if (constraintViolations.size() != 0) {
            throw new ValidationException(violationMessageBuilder.toString());
        }

        return joinPoint.proceed();
    }

    private Set<ConstraintViolation<Object>> getViolationsFromParam(Object methodArg) {
        LOG.debug("Validating argument: {}", methodArg);

        return validator.validate(methodArg);
    }

    private String getViolationMessage(ConstraintViolation<Object> cv) {
        return String.format("property: [%s], value: [%s], message: [%s] \n",
                cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage());
    }
}
