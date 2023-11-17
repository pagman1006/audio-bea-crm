package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInSubBrand implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	private String id;
	@NotEmpty
	private String subBrandName;
	@NotNull
	private DtoInBrand brand;
	private boolean enabled;

}
