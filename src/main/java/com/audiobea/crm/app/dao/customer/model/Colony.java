package com.audiobea.crm.app.dao.customer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "colonies")
public class Colony implements Serializable, Comparable<Colony> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "colony_id")
	private Long id;
	
	private String name;
	private String postalCode;
	
	@Override
	public int compareTo(Colony c) {
		return name.compareTo(c.getName());
	}

}
