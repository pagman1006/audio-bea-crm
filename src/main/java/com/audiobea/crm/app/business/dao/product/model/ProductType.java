package com.audiobea.crm.app.business.dao.product.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "products_type")
public class ProductType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_type_id")
	private Long id;
	
	@Size(min = 3, max = 15)
	@Column(length = 15)
	private String type;

}
