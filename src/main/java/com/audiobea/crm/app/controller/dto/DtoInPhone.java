package com.audiobea.crm.app.controller.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DtoInPhone implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String phoneNumber;
	private EnumPhoneType phoneType;
	private boolean enabled;
}
