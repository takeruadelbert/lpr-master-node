package com.stn.ester.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MaxUploadFileExceededException {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleMaxUploadException(MaxUploadSizeExceededException e) {
        Map<String, Object> errorMessage = new HashMap<>();
        errorMessage.put("status", HttpStatus.NOT_ACCEPTABLE.value());
        errorMessage.put("message", e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_ACCEPTABLE);
    }
}
