package com.audiobea.crm.app.dao.demographic.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document("address")
public class Address implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String street;
	private String outdoorNumber;
	private String indoorNumber;

	@DocumentReference
	private State state;

	@DocumentReference
	private City city;

	@DocumentReference
	private Colony colony;

}
