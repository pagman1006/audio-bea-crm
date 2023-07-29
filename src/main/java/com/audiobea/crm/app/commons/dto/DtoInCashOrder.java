package com.audiobea.crm.app.commons.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DtoInCashOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private DtoInProduct product;
	private Integer quantity;


}
