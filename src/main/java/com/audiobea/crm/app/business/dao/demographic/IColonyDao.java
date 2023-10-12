package com.audiobea.crm.app.business.dao.demographic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.demographic.model.Colony;
import com.audiobea.crm.app.utils.Constants;

public interface IColonyDao extends PagingAndSortingRepository<Colony, Long> {

	@Query(value = Constants.FIND_COLONIES, nativeQuery = true)
	Page<Colony> findAllByColonyOrPostalCode(String colony, String postalCode, Pageable pageable);

	@Query(value = Constants.FIND_COLONIES_BY_CITY_ID, nativeQuery = true)
	Page<Colony> findByCityId(Long cityId, String colony, String postalCode, Pageable pageable);

	@Query(value = Constants.FIND_COLONIES_BY_STATE_ID, nativeQuery = true)
	Page<Colony> findByStateId(Long stateId, String colony, String postalCode, Pageable pageable);

//	@Query(value = Constants.FIND_COLONIES_BY_STATE_ID_CITY_ID, nativeQuery = true)
//	Page<Colony> findByStateIdAndCityId(Long stateId, Long cityId, String postalCode, Pageable pageable);

	@Query(value = Constants.FIND_COLONIES_BY_STATE_ID_CITY_ID_CODE_POSTAL, nativeQuery = true)
	Page<Colony> findByStateIdAndCityAndCodePostalId(Long stateId, Long cityId, String colony, String postalCode, Pageable pageable);

	@Query(value = Constants.FIND_COLONIES_BY_STATE_NAME_CITY_NAME_COLONY_NAME_CODE_POSTAL, nativeQuery = true)
	Page<Colony> findAllByStateNameAndCityNameAndColonyNameCodePostal(String state, String city, String colony, String postalCode,
			Pageable pageable);
}
