package com.nnk.springboot.exceptions;
/**
 * Exception for CurvePoint entity
 */
public class CurvePointNotFoundException extends RuntimeException {
    
    public CurvePointNotFoundException(String message) {
        super(message);
    }
}
