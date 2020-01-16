package com.stn.ester.core.exceptions;

public class MultipleLoginException extends RuntimeException {

    public MultipleLoginException() {
        super("Another Device Just Login");
    }

    public MultipleLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleLoginException(String message) {
        super(message);
    }

    public MultipleLoginException(Throwable cause) {
        super(cause);
    }
}
