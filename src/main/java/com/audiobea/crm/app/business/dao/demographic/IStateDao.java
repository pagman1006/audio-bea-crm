package com.audiobea.crm.app.business.dao.demographic;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;

import com.audiobea.crm.app.business.dao.demographic.model.State;

public interface IStateDao extends PagingAndSortingRepository<State, Long> {

	public State findByName(String name);
	
	@Async
	public <S extends State> List<S> saveAll(Iterable<S> entities);
	
	@Async
	public <S extends State> S save(S entity);
}
