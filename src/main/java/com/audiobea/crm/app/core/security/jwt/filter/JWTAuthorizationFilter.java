package com.audiobea.crm.app.core.security.jwt.filter;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.audiobea.crm.app.core.security.jwt.business.IJWTService;
import com.audiobea.crm.app.core.security.jwt.business.impl.JWTServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final IJWTService jwtService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, IJWTService jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		log.debug("doFilterInternal");
		response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
		response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
		String header = request.getHeader(JWTServiceImpl.HEADER_STRING);
		log.debug("Header: {}", header);
		if (!requiresAuthentication(header)) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = null;

		if (jwtService.validate(header)) {
			authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header), null, jwtService.getRoles(header));
			authentication.getAuthorities().forEach(authority -> log.debug(authority.toString()));
		}

		if (authentication != null && StringUtils.isNotBlank(authentication.getName())) {
			log.debug("Authentication: {}", authentication.getName());
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		log.debug("SecurityContextHolder setAuthentication");
		chain.doFilter(request, response);
		log.debug("chain.doFilter(request, response)");
	}

	protected boolean requiresAuthentication(String header) {
		log.debug("headerRequiresAuthentication: {}", !(header == null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX)));
		return !(header == null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX));
	}

}
