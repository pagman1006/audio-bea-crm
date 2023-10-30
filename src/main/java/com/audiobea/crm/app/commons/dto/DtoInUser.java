package com.audiobea.crm.app.commons.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@Email
	@NotBlank
	private String username;
	private String password;
	private boolean enabled;
	
	private List<DtoInRole> roles;
}
