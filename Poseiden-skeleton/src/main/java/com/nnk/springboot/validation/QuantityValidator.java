package com.nnk.springboot.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validation class for quantity of type double in java
 * A quantity can be null, must be positive , not zero and equal or less than
 * 1000 with maximum 4 digits before decimal point and only one or zero digit
 * after.
 * By default, quantity is always a double value
 */
public class QuantityValidator implements ConstraintValidator<QuantityConstraint, Double> {
    private static final String QUANTITY_PATTERN = "^\\+?\\d{1,4}(\\.\\d)$";

    private static final Pattern pattern = Pattern.compile(QUANTITY_PATTERN);

    @Override
    public void initialize(QuantityConstraint quantityConstraint) {
        // instanciation of the given constraint declaration
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return (pattern.matcher(String.valueOf(value)).matches()) && (value <= 1000) && (value > 0);
    }
}
