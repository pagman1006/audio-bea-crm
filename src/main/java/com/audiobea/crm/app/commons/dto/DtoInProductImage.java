package com.audiobea.crm.app.commons.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInProductImage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String imageName;
	private boolean selected;
	
	public DtoInProductImage() {
		super();
	}
	
	public DtoInProductImage(String imageName) {
		super();
		this.imageName = imageName;
	}

}
