package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for CurvePoint entity
 */
@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class CurvePointNotFoundException extends RuntimeException {
    
    public CurvePointNotFoundException(String message) {
        super(message);
    }
}
