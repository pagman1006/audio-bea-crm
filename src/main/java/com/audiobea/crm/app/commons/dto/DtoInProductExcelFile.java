package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInProductExcelFile implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String brand;
	private String subBrand;
	private String description;
	private Double discount;
	private Boolean enabled;
	private Boolean productNew;
	private Double price;
	private String productName;
	private Integer stock;
	private String title;
	private String type;

}
