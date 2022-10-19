package com.nnk.springboot.exceptions;

public class CurvePointNotfoundException extends RuntimeException {
    
    public CurvePointNotfoundException(String message) {
        super(message);
    }
}
