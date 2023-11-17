package com.audiobea.crm.app.dao.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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
	private ProductType productType;

	@DocumentReference
	private List<ProductImage> images;

	@DocumentReference
	private List<ProductRanking> rankings;

	private String brandId;
	private String subBrandId;

	private boolean productNew;
	private boolean enabled;

}
