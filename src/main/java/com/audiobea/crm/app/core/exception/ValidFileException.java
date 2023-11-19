package com.audiobea.crm.app.core.exception;

import java.io.Serial;

public class ValidFileException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public ValidFileException(String message) {
		super(message);
	}
}
