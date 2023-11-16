package com.audiobea.crm.app.dao.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document("brands")
public class Brand implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String brandName;
	private boolean enabled;

}
