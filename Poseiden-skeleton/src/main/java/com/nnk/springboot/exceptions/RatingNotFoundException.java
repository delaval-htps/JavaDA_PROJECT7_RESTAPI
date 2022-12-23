package com.nnk.springboot.exceptions;

/**
 * Exception for Rating entity
 */
public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(String message) {
        super(message);
    }
}
