package com.nnk.springboot.exceptions;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GlobaleErrorResponse {
 
    private Timestamp date;
    private int status;
    private String error;
    private String errorMessage;

    public GlobaleErrorResponse(Timestamp date, int status, String error, String errorMessage) {
        this.date = date;
        this.status = status;
        this.error = error;
        this.errorMessage = errorMessage;
    }
    
}
