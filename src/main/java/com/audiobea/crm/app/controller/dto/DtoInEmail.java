package com.audiobea.crm.app.controller.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DtoInEmail implements Serializable {

	private static final long serialVersionUID = 1L;
	
private Long id;
	
	private String email;
	private EnumEmailType emailType;
	
	private boolean enabled;

}
