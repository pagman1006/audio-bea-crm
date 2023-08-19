package com.audiobea.crm.app.core.security.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityMixin {

	@JsonCreator
	protected SimpleGrantedAuthorityMixin(@JsonProperty("authority") String role) {}
}
