package com.stn.ester.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class EmptyFieldException extends RuntimeException {
    public EmptyFieldException() {
        super("Column is empty!");
    }

    public EmptyFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyFieldException(String message) {
        super(message);
    }

    public EmptyFieldException(Throwable cause) {
        super(cause);
    }
}
