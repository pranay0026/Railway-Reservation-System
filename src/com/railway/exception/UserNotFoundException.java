package com.railway.exception;

public class UserNotFoundException extends RailwayException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
