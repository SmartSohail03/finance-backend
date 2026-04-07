package com.finance.exception;

// Thrown when an inactive user tries to perform any action (403)
public class InactiveUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    public InactiveUserException(String message) {
        super(message);
    }
}