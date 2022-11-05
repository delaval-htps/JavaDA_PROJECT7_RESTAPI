package com.nnk.springboot.exceptions;

public class RuleNameNotFoundException extends RuntimeException {

    /**
     * @param message
     */
    public RuleNameNotFoundException(String message) {
        super(message);
    }
    
    
}
