package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInFileResponse {

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
