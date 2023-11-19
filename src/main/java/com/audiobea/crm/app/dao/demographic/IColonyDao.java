package com.audiobea.crm.app.dao.demographic;

import com.audiobea.crm.app.dao.demographic.model.Colony;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IColonyDao extends MongoRepository<Colony, String> {

	@Query(value = "{'name': {$regex: ?0, $options: 'i'}, 'postalCode': {$regex: ?1}}")
	Page<Colony> findAllByColonyOrPostalCode(String colonyName, String postalCode, Pageable pageable);

	@Query(value = "{'_id': {'$in': ?0}, 'name': {$regex: ?1, $options: 'i'}, 'postalCode': {$regex: ?2}}")
	Page<Colony> findByCityId(List<String> names, String colonyName, String postalCode, Pageable pageable);

	@Query(value = "{'cityId': ?0, 'name': {$regex: ?1, $options: 'i'}, 'postalCode': {$regex: ?2}}")
	Page<Colony> findByCityId(String cityId, String colonyName, String postalCode, Pageable pageable);

	@Query(value = "{'stateId': ?0, 'cityId': ?1, 'name': {$regex: ?2, $options: 'i'}, 'postalCode': {$regex: ?3}}")
	Page<Colony> findByStateIdAndCityId(String stateId, String cityId, String colonyName, String postalCode, Pageable pageable);
}
