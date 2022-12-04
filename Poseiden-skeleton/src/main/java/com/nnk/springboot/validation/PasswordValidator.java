package com.nnk.springboot.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w])(?=.*[\\-!@#&()\\[\\{\\}\\]:;',?/*~$^+=<>]).{8,}$";

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
