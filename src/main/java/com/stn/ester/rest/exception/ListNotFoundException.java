package com.stn.ester.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ListNotFoundException extends RuntimeException{
    public ListNotFoundException() {
        super("List Not Found.");
    }

    public ListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListNotFoundException(String message) {
        super(message);
    }

    public ListNotFoundException(Throwable cause) {
        super(cause);
    }
}
