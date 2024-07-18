package com.ankitkumar.library.exception;

public class BookRegistrationException extends RuntimeException {

	public BookRegistrationException(String message) {
		super(message);
	}
	
	public BookRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
