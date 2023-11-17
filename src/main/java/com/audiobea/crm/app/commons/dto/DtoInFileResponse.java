package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInFileResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

    private String colonies;
    private String cities;
	private String states;
	
	public DtoInFileResponse(String states, String cities, String colonies) {
		super();
		this.colonies = colonies;
		this.cities = cities;
		this.states = states;
	}
	
}
