package com.audiobea.crm.app.core.security.jwt.business.impl;

import com.audiobea.crm.app.core.security.jwt.SimpleGrantedAuthorityMixin;
import com.audiobea.crm.app.core.security.jwt.business.IJWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Service
public class JWTServiceImpl implements IJWTService {

	public static final String SECRET = Base64.getEncoder().encodeToString("Some.Key.Secret.123456".getBytes());

	public static final long EXPIRATION_DATE = 604800000L;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";

	@Override
	public String create(Authentication auth) throws IOException {
		log.debug("create authentication");
		String username = ((User) auth.getPrincipal()).getUsername();
		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		return Jwts.builder().setClaims(claims).setSubject(username).signWith(getSigningKey()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE)).compact();
	}

	@Override
	public boolean validate(String token) {
		log.debug("validate authentication");
		try {
			getClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(resolve(token)).getBody();
	}

	@Override
	public String getUsername(String token) {
		log.debug("getUsername authentication");
		return getClaims(token).getSubject();
	}

	@Override
	public Collection<GrantedAuthority> getRoles(String token) throws IOException {
		log.debug("getRoles authentication");
		Object roles = getClaims(token).get("authorities");
		return Arrays.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
				.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
	}

	@Override
	public String resolve(String token) {
		log.debug("getRoles authentication");
		if (token != null && token.startsWith(TOKEN_PREFIX)) {
			return token.replace(TOKEN_PREFIX, "");
		}
		return null;
	}

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	}

}
