package com.nnk.springboot.exceptions;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler  {
    
    @ExceptionHandler
    public ResponseEntity<GlobaleErrorResponse> handleGlobalPoseidonExecption(Exception e,HttpServletRequest request) {
        GlobaleErrorResponse errorResponse = new GlobaleErrorResponse();

        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setDate(new Timestamp(System.currentTimeMillis()));
        errorResponse.setErrorMessage(e.getMessage());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

        log.error("Status:{} {} ; Request: {} {}; CauseBy:{} ",
        errorResponse.getStatus(),
        errorResponse.getError(),
        request.getMethod(),
        request.getRequestURI(),
        errorResponse.getErrorMessage());

        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

}
