package com.stn.ester.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY)
public class SimpleSearchException extends RuntimeException {

    public SimpleSearchException(String message) {
        super(message);
    }

}
