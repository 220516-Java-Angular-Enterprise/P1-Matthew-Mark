package com.revature.shoe.util.custom_exceptions;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
