package com.audiobea.crm.app.dao.demographic;

import com.audiobea.crm.app.dao.demographic.model.State;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IStateDao extends MongoRepository<State, String>{

	@Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
	List<State> findAllByName(String name);

	@Query(value = "{'name': ?0}")
	Optional<State> findByName(String name);
}
