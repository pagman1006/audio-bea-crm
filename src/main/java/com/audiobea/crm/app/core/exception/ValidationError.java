package com.audiobea.crm.app.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ValidationError {

	private final String field;
	private final String message;
}
