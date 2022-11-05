package com.nnk.springboot.exceptions;

public class TradeNotFoundException extends RuntimeException{

    /**
     * @param message
     */
    public TradeNotFoundException(String message) {
        super(message);
    }
    
}
