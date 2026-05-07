package com.ricardo.GymPass.domain.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String resourceName;

    public ResourceNotFoundException(String resourceName, String id) {
        super(String.format("%s not found with id: %s", resourceName, id));
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}