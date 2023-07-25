package com.audiobea.crm.app.controller.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DtoInColony implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String postalCode;
	
	private DtoInCity city;
	private DtoInState state;

}
