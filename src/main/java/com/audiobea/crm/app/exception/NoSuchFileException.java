package com.audiobea.crm.app.exception;

import java.io.IOException;

public class NoSuchFileException extends IOException {

	private static final long serialVersionUID = 1L;
	
	public NoSuchFileException(String message) {
		super(message);
	}

}
