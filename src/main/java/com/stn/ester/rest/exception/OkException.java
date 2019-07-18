package com.stn.ester.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.OK)
public class OkException extends RuntimeException {
    public OkException() {
        super("Data is successfully created.");
    }

    public OkException(String message, Throwable cause) {
        super(message, cause);
    }

    public OkException(String message) {
        super(message);
    }

    public OkException(Throwable cause) {
        super(cause);
    }
}
