package com.nnk.springboot.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = QuantityValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface QuantityConstraint {
    String message() default "Invalid Quantity: must be a decimal digit between 0 excluded and 1000 followed by a maximum one digit after the decimal point";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
