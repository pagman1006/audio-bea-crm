package com.audiobea.crm.app.controller.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DtoInUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String username;
	private String password;
	private boolean enabled;
	
	private List<DtoInRole> roles;
}
