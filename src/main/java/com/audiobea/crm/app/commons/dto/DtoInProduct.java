package com.audiobea.crm.app.commons.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DtoInProduct implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String productName;
	private Double price;
	private Double discount;
	private String title;
	private String description;
	private Integer stock;
	private List<DtoInProductImage> images;
	private DtoInSubBrand subBrand;
	private boolean enabled;

}
