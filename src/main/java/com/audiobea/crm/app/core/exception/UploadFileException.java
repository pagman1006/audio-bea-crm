package com.audiobea.crm.app.core.exception;

import java.io.Serial;

public class UploadFileException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public UploadFileException(String message) {
		super(message);
	}
}
