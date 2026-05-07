package com.ricardo.GymPass.domain.exception;

public class ExternalApiException extends RuntimeException {
    private final int statusCode;

    public ExternalApiException(String message) {
        super(message);
        this.statusCode = 0;
    }

    public ExternalApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}