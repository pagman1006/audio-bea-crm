package com.audiobea.crm.app.commons.dto;

import com.audiobea.crm.app.dao.product.model.ProductRanking;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DtoInProduct implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String productName;
	private Double price;
	private Double discount;
	private String title;
	private String description;
	private Integer stock;
	private DtoInProductType productType;
	private List<DtoInProductImage> images;
	private List<ProductRanking> rankings;
	private DtoInSubBrand subBrand;
	private boolean newProduct;
	
	private boolean enabled;

}
