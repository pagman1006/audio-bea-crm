package com.audiobea.crm.app.dao.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Document("brands")
public class Brand implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	@Indexed(unique = true)
	private String brandName;

	@DocumentReference(lazy = true)
	private List<SubBrand> subBrands;

	@DocumentReference(lazy = true)
	private List<Product> products;
	private boolean enabled;

}
