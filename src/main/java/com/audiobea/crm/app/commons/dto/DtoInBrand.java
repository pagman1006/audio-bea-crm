package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(Include.NON_EMPTY)
public class DtoInBrand implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	private String id;
	@NotEmpty
	private String brandName;
	List<DtoInSubBrand> subBrands;
	List<DtoInProduct> products;
	private boolean enabled;

}
