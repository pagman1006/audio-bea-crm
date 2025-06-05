package com.audiobea.crm.app.core.exception;

import java.io.Serial;

public class NoSuchElementFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public NoSuchElementFoundException(String message) {
		super(message);
	}
}
