package com.stn.ester.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
public class LicenseExpiredException extends RuntimeException {
    public LicenseExpiredException() {
        super("License expired");
    }

    public LicenseExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public LicenseExpiredException(String message) {
        super(message);
    }

    public LicenseExpiredException(Throwable cause) {
        super(cause);
    }
}
