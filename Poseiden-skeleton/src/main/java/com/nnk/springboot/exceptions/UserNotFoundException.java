package com.nnk.springboot.exceptions;
/**
 * Exception for User entity
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * @param message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
    
}
