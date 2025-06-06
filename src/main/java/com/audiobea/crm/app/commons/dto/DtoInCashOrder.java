package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInCashOrder implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private String id;
	private DtoInProduct product;
	private Integer quantity;


}
