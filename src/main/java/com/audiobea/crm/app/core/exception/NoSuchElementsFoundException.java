package com.audiobea.crm.app.core.exception;

import java.io.Serial;

public class NoSuchElementsFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public NoSuchElementsFoundException(String message) {
		super(message);
	}
}
