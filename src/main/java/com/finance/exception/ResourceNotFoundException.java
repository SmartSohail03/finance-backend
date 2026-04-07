package com.finance.exception;

// Thrown when something is not found in the database (404)
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
