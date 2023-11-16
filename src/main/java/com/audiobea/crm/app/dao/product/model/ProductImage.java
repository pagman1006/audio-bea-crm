package com.audiobea.crm.app.dao.product.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Document("products_images")
public class ProductImage implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String imageName;
	private boolean selected;

	public ProductImage(String imageName) {
	}
}
