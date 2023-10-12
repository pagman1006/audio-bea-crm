package com.audiobea.crm.app.core.security.jwt.business;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;

public interface IJWTService {

	public String create(Authentication auth) throws IOException;

	public boolean validate(String token);

	public Claims getClaims(String token);

	public String getUsername(String token);

	public Collection<GrantedAuthority> getRoles(String token) throws IOException;

	public String resolve(String token);

}
