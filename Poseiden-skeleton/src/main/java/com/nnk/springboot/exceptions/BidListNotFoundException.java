package com.nnk.springboot.exceptions;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BidListNotFoundException extends RuntimeException {
    
    public BidListNotFoundException(String message) {
        super(message);
        log.error(message,this);
    }

    
}
