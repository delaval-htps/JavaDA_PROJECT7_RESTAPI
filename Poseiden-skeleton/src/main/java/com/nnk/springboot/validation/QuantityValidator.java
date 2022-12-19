package com.nnk.springboot.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class QuantityValidator implements ConstraintValidator<QuantityConstraint, Double> {
    private static final String QUANTITY_PATTERN = "^\\d{1,4}(\\.\\d)$";

    private static final Pattern pattern = Pattern.compile(QUANTITY_PATTERN);

    @Override
    public void initialize(QuantityConstraint quantityConstraint) {
        // instanciation of the given constraint declaration
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
       return (pattern.matcher(String.valueOf(value)).matches());
    }
}
