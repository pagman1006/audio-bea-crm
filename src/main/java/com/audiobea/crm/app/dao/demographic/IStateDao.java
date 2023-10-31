package com.audiobea.crm.app.dao.demographic;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.demographic.model.State;

public interface IStateDao extends PagingAndSortingRepository<State, Long> {

	public State findByName(String name);
	
}
