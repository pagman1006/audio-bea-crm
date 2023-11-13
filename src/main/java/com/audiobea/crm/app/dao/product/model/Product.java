package com.audiobea.crm.app.dao.product.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@Column(length = 60)
	private String productName;

	private Double price;
	private Double discount;
	
	@Column(length = 60)
	private String title;
	private String description;
	private Integer stock;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "product_type_id")
	private ProductType productType;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private List<ProductImage> images;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private List<ProductRanking> rankings;
	
	@ManyToOne
	@JoinColumn(name = "sub_brand_id")
	private SubBrand subBrand;
	
	private boolean newProduct;
	
	private boolean enabled;

}
