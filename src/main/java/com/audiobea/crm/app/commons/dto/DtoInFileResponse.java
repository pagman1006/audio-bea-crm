package com.audiobea.crm.app.commons.dto;

import lombok.Data;

@Data
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
