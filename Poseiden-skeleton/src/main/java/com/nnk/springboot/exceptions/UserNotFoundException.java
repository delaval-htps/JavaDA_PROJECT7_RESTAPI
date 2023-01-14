package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for User entity
 */
@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends RuntimeException {

    /**
     * @param message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
    
}
