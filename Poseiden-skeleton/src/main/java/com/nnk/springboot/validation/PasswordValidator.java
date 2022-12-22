package com.nnk.springboot.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

/**
 * Validation class for password. A password must contains at least one capital
 * letter, one symbol,one number and must be 8 characters long
 */
public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\-!@#&()\\[\\{\\}\\]:;',?/*~$^+=<>]).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void initialize(PasswordConstraint passwordContraint) {
        // instanciation of the given constraint declaration
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (pattern.matcher(value).matches() && !StringUtils.containsWhitespace(value));
    }
}
