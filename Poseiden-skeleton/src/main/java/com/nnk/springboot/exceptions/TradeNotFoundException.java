package com.nnk.springboot.exceptions;
/**
 * Exception for Trade entity
 */
public class TradeNotFoundException extends RuntimeException{

    /**
     * @param message
     */
    public TradeNotFoundException(String message) {
        super(message);
    }
    
}
