package com.finance.exception;

// Thrown when a user tries to do something their role doesn't allow (403)
public class AccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    public AccessDeniedException(String message) {
        super(message);
    }
}
