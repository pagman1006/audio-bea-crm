package com.audiobea.crm.app.core.exception;

import java.io.Serial;

public class ParseFileException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public ParseFileException(String message) {
		super(message);
	}
}
