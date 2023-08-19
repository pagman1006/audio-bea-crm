package com.audiobea.crm.app.business.dao.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.customer.model.Colony;

public interface IColonyDao extends PagingAndSortingRepository<Colony, Long> {

	@Query(value = "SELECT * FROM colonies WHERE colonies.city_id IN (SELECT DISTINCT ci.city_id FROM cities ci WHERE ci.state_id = :stateId AND ci.city_id = :cityId)", nativeQuery = true)
	Page<Colony> findByStateIdAndCityId(Long stateId, Long cityId, Pageable pageable);

	@Query(value = "SELECT * FROM colonies WHERE colonies.city_id IN (SELECT DISTINCT ci.city_id FROM cities ci WHERE ci.state_id = :stateId AND ci.city_id = :cityId) AND colonies.postal_code = :codePostal", nativeQuery = true)
	Page<Colony> findByStateIdAndCityAndCodePostalId(Long stateId, Long cityId, String codePostal, Pageable pageable);

	@Query(value = "SELECT * FROM colonies WHERE city_id IN (SELECT city_id FROM cities WHERE state_id IN (SELECT id FROM states WHERE name LIKE %:state%) AND name like %:city%) AND name LIKE %:colony% AND postal_code LIKE %:postalCode%", nativeQuery = true)
	Page<Colony> findAllByStateNameAndCityNameAndColonyNameCodePostal(String state, String city, String colony,
			String postalCode, Pageable pageable);
}
