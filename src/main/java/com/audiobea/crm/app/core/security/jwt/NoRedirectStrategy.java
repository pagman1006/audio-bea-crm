package com.audiobea.crm.app.core.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.RedirectStrategy;

@Slf4j
public class NoRedirectStrategy implements RedirectStrategy {

	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) {
		log.debug("NoRedirectStrategy");
	}


}
