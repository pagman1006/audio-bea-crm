package com.audiobea.crm.app.core.security.jwt.business;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

public interface IJWTService {

    String create(Authentication auth) throws IOException;

    boolean validate(String token);

    Claims getClaims(String token);

    String getUsername(String token);

    Collection<GrantedAuthority> getRoles(String token) throws IOException;

    String resolve(String token);

}
