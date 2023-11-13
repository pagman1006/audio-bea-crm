package com.audiobea.crm.app.dao.product.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Entity
@Table(name = "products_images")
public class ProductImage implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long id;
	
	@Column(length = 60)
	private String imageName;
	
	private boolean selected;

	public ProductImage(String imageName) {
	}
}
