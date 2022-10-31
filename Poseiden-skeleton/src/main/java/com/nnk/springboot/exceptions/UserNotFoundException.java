package com.nnk.springboot.exceptions;

public class UserNotFoundException extends RuntimeException {

    /**
     * @param message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
    
}
