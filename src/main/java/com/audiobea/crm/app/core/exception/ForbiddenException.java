package com.audiobea.crm.app.core.exception;

import java.io.Serial;

public class ForbiddenException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public ForbiddenException(String message) {
		super(message);
	}

}
