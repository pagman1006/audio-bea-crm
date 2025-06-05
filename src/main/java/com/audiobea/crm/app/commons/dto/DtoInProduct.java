package com.audiobea.crm.app.commons.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class DtoInProduct implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private String id;
	@NotEmpty
	private String productName;
	private Double price;
	private Double discount;
	private String title;
	private String description;
	private Integer stock;
	private DtoInProductType productType;
	private List<DtoInProductImage> images;
	private List<DtoInProductRanking> rankings;
	private DtoInBrand brand;
	private DtoInSubBrand subBrand;
	private boolean productNew;
	
	private boolean enabled;

}
