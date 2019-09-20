package com.stn.ester.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FilterOperationException extends IllegalArgumentException {
    public FilterOperationException() {
        super("Invalid Operation.");
    }

    public FilterOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterOperationException(String message) {
        super("Operation '" + message + "' not found.");
    }

    public FilterOperationException(Throwable cause) {
        super(cause);
    }
}
