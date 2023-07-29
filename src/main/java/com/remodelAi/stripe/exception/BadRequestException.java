package com.remodelAi.stripe.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String s) {
        super(s);
    }
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
