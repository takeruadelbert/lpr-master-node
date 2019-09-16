package com.stn.ester.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class SessionExpireException extends RuntimeException {
    public SessionExpireException() {
        super();
    }

    public SessionExpireException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionExpireException(String message) {
        super(message);
    }

    public SessionExpireException(Throwable cause) {
        super(cause);
    }
}
