package com.finance.exception;

// Thrown when trying to create a user with an already existing email (400)
public class DuplicateEmailException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    public DuplicateEmailException(String message) {
        super(message);
    }
}