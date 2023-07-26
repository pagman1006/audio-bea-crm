package com.audiobea.crm.app.commons.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DtoInBrand implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String brandName;
	private List<DtoInSubBrand> subBrands;
	private boolean enabled;

}
