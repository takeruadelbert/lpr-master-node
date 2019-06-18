package com.stn.ester.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ConfirmNewPasswordException extends RuntimeException {
    public ConfirmNewPasswordException() {
        super("New Password and Retype Password is different.");
    }

    public ConfirmNewPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfirmNewPasswordException(String message) {
        super(message);
    }

    public ConfirmNewPasswordException(Throwable cause) {
        super(cause);
    }
}
