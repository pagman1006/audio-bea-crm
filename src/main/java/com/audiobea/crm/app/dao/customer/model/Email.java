package com.audiobea.crm.app.dao.customer.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document("emails")
public class Email implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	@Indexed(unique = true)
	private String emailAddress;
	private EmailType emailType;
	private boolean enabled;

}
