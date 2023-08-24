package com.audiobea.crm.app.commons.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String username;
	private String password;
	private boolean enabled;
	
	private List<DtoInRole> roles;
}
