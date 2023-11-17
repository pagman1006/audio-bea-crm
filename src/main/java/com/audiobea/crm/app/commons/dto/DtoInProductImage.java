package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInProductImage implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String id;
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
