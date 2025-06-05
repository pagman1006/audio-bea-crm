package com.audiobea.crm.app.core.exception;

import org.springframework.dao.DataIntegrityViolationException;

import java.io.Serial;

public class DuplicateRecordException extends DataIntegrityViolationException {

	@Serial
	private static final long serialVersionUID = 1L;

	public DuplicateRecordException(String msg) {
		super(msg);
	}

}
