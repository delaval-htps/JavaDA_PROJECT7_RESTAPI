package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for Trade entity
 */
@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class TradeNotFoundException extends RuntimeException{

    /**
     * @param message
     */
    public TradeNotFoundException(String message) {
        super(message);
    }
    
}
