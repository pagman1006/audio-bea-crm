package com.audiobea.crm.app.dao.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document("images")
public class Image implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String img;

}
