package com.audiobea.crm.app.dao.demographic;

import com.audiobea.crm.app.dao.demographic.model.State;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IStateDao extends MongoRepository<State, String>{

	@Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
	List<State> findAllByName(String name);

}
