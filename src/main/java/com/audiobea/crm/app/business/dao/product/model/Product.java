package com.audiobea.crm.app.business.dao.product.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@Size(min = 3, max = 60)
	@Column(length = 60)
	private String productName;

	private Double price;
	private Double discount;
	
	@Size(min = 3, max = 60)
	@Column(length = 60)
	private String title;
	private String description;
	private Integer stock;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private List<ProductImage> images;
	
	@ManyToOne
	@JoinColumn(name = "sub_brand_id")
	private SubBrand subBrand;
	
	private boolean newProduct;
	
	private boolean enabled;

}
