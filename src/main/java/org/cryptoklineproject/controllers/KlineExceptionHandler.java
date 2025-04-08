package org.cryptoklineproject.controllers;

import org.cryptoklineproject.model.exception.InputParamInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ControllerAdvice
public class KlineExceptionHandler {

    @ExceptionHandler({InputParamInvalidException.class})
    public ResponseEntity<String> invalidParamHandler(InputParamInvalidException exception){
        String issue = exception.getMessage();
        return new ResponseEntity<>(issue, HttpStatus.BAD_REQUEST);
    }
}
