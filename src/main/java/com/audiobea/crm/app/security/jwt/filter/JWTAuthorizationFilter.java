package com.audiobea.crm.app.security.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.audiobea.crm.app.security.jwt.business.IJWTService;
import com.audiobea.crm.app.security.jwt.business.impl.JWTServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private IJWTService jwtService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, IJWTService jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		log.debug("doFilterInternal");
		String header = request.getHeader(JWTServiceImpl.HEADER_STRING);
		log.debug("Header: {}", header);
		if (!requiresAuthentication(header)) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = null;
		
		if(jwtService.validate(header)) {
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
		log.debug("headerRequiresAutentication: {}", !(header == null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX)));
		return (header == null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX)) ? false : true;
	}

}
