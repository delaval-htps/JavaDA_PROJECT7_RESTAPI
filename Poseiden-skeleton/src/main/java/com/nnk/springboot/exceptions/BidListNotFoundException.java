package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for Bidlist entity
 */
@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class BidListNotFoundException extends RuntimeException {
    

    public BidListNotFoundException(String message) {
        super(message);
    }

    
}
