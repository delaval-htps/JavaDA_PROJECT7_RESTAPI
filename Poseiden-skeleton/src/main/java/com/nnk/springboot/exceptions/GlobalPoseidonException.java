package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception not corresponding to a entity's error
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class GlobalPoseidonException extends RuntimeException {
    
    
    public GlobalPoseidonException(String message) {
        super(message);
    }
}
