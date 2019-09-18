package com.stn.ester.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class FilterException extends IllegalArgumentException {
    public FilterException() {
        super("Invalid Parameter Field.");
    }

    public FilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterException(String message) {
        super("Field '" + message + "' doesn't exist.");
    }

    public FilterException(Throwable cause) {
        super(cause);
    }
}
