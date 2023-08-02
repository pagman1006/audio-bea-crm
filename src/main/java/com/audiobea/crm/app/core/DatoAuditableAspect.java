 package com.audiobea.crm.app.core;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DatoAuditableAspect {

	@After("@annotation(com.audiobea.crm.app.core.DatoAuditable)")
	public void datoAuditable(JoinPoint joinPoint) {
		
	}
	
}
