package com.nnk.springboot.exceptions;

public class CurvePointNotFoundException extends RuntimeException {
    
    public CurvePointNotFoundException(String message) {
        super(message);
    }
}
