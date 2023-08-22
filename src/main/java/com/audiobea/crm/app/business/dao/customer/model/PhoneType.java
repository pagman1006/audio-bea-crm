package com.audiobea.crm.app.business.dao.customer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;


@Data
@Entity
@Table(name = "phones_type")
public class PhoneType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "phone_type_id")
	private Long id;
	
	@Size(min = 3, max = 15)
	@Column(length = 15)
	private String type;
}