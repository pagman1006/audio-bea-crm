package com.audiobea.crm.app.dao.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Document("products")
public class Product implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String productName;
	private Double price;
	private Double discount;
	private String title;
	private String description;
	private Integer stock;

	@DBRef
	private ProductType productType;

	@DBRef
	private List<ProductImage> images;

	@DBRef
	private List<ProductRanking> rankings;

	@DBRef
	private SubBrand subBrand;
	private boolean newProduct;
	private boolean enabled;

}
