package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInFileResponse {

    private Long colonies;
    private Long cities;
	private Long states;
	
	public DtoInFileResponse(Long states, Long cities, Long colonies) {
		super();
		this.colonies = colonies;
		this.cities = cities;
		this.states = states;
	}
	
}
