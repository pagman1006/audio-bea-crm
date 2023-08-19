package com.audiobea.crm.app.core.exception;

public class NoSuchElementsFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NoSuchElementsFoundException(String message) {
		super(message);
	}
}
