package com.audiobea.crm.app.exception;

public class NoSuchElementsFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NoSuchElementsFoundException(String message) {
		super(message);
	}
}
