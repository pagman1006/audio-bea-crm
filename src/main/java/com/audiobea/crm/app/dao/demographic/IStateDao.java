package com.audiobea.crm.app.dao.demographic;

import com.audiobea.crm.app.dao.demographic.model.State;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IStateDao extends PagingAndSortingRepository<State, Long>, CrudRepository<State, Long> {

	State findByName(String name);
	
}
