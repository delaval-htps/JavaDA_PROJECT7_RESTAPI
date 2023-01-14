package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for Rulename entity
 */
@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class RuleNameNotFoundException extends RuntimeException {

    /**
     * @param message
     */
    public RuleNameNotFoundException(String message) {
        super(message);
    }
    
    
}
