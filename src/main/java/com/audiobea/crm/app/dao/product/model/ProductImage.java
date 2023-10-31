package com.audiobea.crm.app.dao.product.model;

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
@Table(name = "products_images")
public class ProductImage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long id;
	
	@Size(min = 3, max = 60)
	@Column(length = 60)
	private String imageName;
	
	private boolean selected;

	public ProductImage() {
		super();
	}
	
	public ProductImage(@Size(min = 3, max = 60) String imageName) {
		super();
		this.imageName = imageName;
	}
	
}
