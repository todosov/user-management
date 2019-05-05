package com.oleksii.usermanagement.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("RealmNotFound");
    }
}
