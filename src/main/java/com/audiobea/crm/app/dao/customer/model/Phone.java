package com.audiobea.crm.app.dao.customer.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document("phones")
public class Phone implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String phoneNumber;
	private PhoneType phoneType;
	private boolean enabled;
}
