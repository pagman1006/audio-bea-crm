package com.audiobea.crm.app.commons.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DtoInAddress implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String street;
	private String outdoorNumber;
	private String indoorNumber;
	
	private DtoInColony colony;
}
