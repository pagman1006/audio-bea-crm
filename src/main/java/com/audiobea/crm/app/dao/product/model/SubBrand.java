package com.audiobea.crm.app.dao.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document("sub_brands")
public class SubBrand implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String subBrandName;

	@DBRef
	private Brand brand;
	private boolean enabled;

}
