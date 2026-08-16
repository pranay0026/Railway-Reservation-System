package com.railway.exception;

public class CancellationException extends RailwayException {
    public CancellationException(String message) {
        super(message);
    }

    public CancellationException(String message, Throwable cause) {
        super(message, cause);
    }
}
