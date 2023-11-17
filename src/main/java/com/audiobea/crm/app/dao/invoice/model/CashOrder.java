package com.audiobea.crm.app.dao.invoice.model;

import com.audiobea.crm.app.dao.product.model.Product;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serial;
import java.io.Serializable;

@Data

@Document("cash_orders")
public class CashOrder implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@DocumentReference
	private Product product;
	private Integer quantity;

}
