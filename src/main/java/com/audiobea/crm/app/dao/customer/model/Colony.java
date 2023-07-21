package com.audiobea.crm.app.dao.customer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "colonies")
public class Colony implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "colony_id")
	private Long id;
	
	private String colony;
	private String postalCode;
	
	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;
	
	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;

}
