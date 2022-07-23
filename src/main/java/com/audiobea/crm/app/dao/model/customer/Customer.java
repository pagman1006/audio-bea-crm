package com.audiobea.crm.app.dao.model.customer;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.audiobea.crm.app.dao.model.user.User;

import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "primer_nombre")
	private String firstName;
	
	@Column(name = "segundo_nombre")
	private String secondName;
	
	@Column(name = "apellido_paterno")
	private String firstLastName;
	
	@Column(name = "apellido_materno")
	private String secondLastName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	private User user;
	
}
