package com.nnk.springboot.exceptions;
/**
 * Global exception not corresponding to a entity's error
 */
public class GlobalPoseidonException extends RuntimeException {
    
    
    public GlobalPoseidonException(String message) {
        super(message);
    }
}
