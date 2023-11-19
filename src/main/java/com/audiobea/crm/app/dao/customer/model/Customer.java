package com.audiobea.crm.app.dao.customer.model;

import com.audiobea.crm.app.dao.demographic.model.Address;
import com.audiobea.crm.app.dao.invoice.model.Invoice;
import com.audiobea.crm.app.dao.user.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Document("customers")
public class Customer implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String name;
	private String lastName;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	private boolean enabled;

	@DocumentReference
	private User user;

	@DocumentReference
	private List<Address> address;

	@DocumentReference
	private List<Phone> phones;

	@DocumentReference
	private List<Email> emails;

	@DocumentReference
	private List<Invoice> invoices;

}
