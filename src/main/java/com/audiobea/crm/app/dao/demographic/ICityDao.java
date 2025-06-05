package com.audiobea.crm.app.dao.demographic;

import com.audiobea.crm.app.dao.demographic.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ICityDao extends MongoRepository<City, String> {

	@Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
	List<City> findAllByName(String name);

	Page<City> findByNameContaining(String name, Pageable pageable);

	@Query(value = "{'stateId': ?0, 'name': {'$regex': ?1, '$options': 'i'}}")
	Page<City> findByStateIdAndName(String stateId, String name, Pageable pageable);

	@Query(value = "{'_id': {'$in': ?0}}")
	List<City> findAllByStateIdIn(List<String> names);

	@Query(value = "{'stateId': ?0}")
	Page<City> findByStateId(String stateId, Pageable pageable);

}