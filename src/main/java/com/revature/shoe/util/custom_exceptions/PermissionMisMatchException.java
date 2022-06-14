package com.revature.shoe.util.custom_exceptions;

public class PermissionMisMatchException extends RuntimeException{

    public PermissionMisMatchException() {
        super();
    }

    public PermissionMisMatchException(String message) {
        super(message);
    }
}
