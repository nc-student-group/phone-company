package com.phonecompany.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Decorating annotation created with the purpose of avoiding naming clashes
 * between {@link Service} and {@link com.phonecompany.model.Service}
 */
@Service
public @interface ServiceStereotype {
}
