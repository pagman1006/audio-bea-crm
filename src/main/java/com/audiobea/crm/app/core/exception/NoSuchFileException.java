package com.audiobea.crm.app.core.exception;

import java.io.IOException;
import java.io.Serial;

public class NoSuchFileException extends IOException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public NoSuchFileException(String message) {
		super(message);
	}

}
