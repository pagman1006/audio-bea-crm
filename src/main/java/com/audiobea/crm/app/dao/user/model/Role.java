package com.audiobea.crm.app.dao.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "authorities", 
	uniqueConstraints = { 
			@UniqueConstraint(columnNames = 
				{ "user_id", "authority" }) 
			})
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "authority_id")
	private Long id;
	
	@Size(min = 3, max = 15)
	@Column(length = 15)
	private String authority;

}
