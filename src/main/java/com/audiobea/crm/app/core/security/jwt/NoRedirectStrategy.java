package com.audiobea.crm.app.core.security.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.RedirectStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoRedirectStrategy implements RedirectStrategy {

	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		log.debug("NoRedirectStrategy");
	}

}
