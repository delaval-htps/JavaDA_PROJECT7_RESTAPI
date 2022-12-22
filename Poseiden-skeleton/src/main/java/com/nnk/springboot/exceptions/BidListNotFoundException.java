package com.nnk.springboot.exceptions;

/**
 * Exception for Bidlist entity
 */
public class BidListNotFoundException extends RuntimeException {
    

    public BidListNotFoundException(String message) {
        super(message);
    }

    
}
