package com.audiobea.crm.app.commons.dto;

import com.audiobea.crm.app.dao.product.model.ProductRanking;
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
	private String productTypeId;
	private DtoInProductType productType;
	private List<DtoInProductImage> images;
	private List<ProductRanking> rankings;
	private String brandId;
	private String subBrandId;
	private boolean productNew;
	
	private boolean enabled;

}
