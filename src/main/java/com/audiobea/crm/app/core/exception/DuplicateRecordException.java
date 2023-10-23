package com.audiobea.crm.app.core.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateRecordException extends DataIntegrityViolationException {

	private static final long serialVersionUID = 1L;

	public DuplicateRecordException(String msg) {
		super(msg);
	}

}
