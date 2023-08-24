package com.audiobea.crm.app.commons.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInCashOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private DtoInProduct product;
	private Integer quantity;


}
