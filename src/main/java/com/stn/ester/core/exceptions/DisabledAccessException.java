package com.stn.ester.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DisabledAccessException extends RuntimeException {
    public DisabledAccessException() {
        super("Page is disabled");
    }

    public DisabledAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisabledAccessException(String message) {
        super(message);
    }

    public DisabledAccessException(Throwable cause) {
        super(cause);
    }
}
