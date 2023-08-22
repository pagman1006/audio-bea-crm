package com.audiobea.crm.app.business.dao.demographic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.demographic.model.Colony;
import com.audiobea.crm.app.utils.Constants;

public interface IColonyDao extends PagingAndSortingRepository<Colony, Long> {

	@Query(value = Constants.FIND_COLONIES_BY_STATE_ID_CITY_ID, nativeQuery = true)
	Page<Colony> findByStateIdAndCityId(Long stateId, Long cityId, Pageable pageable);

	@Query(value = Constants.FIND_COLONIES_BY_STATE_ID_CITY_ID_CODE_POSTAL, nativeQuery = true)
	Page<Colony> findByStateIdAndCityAndCodePostalId(Long stateId, Long cityId, String codePostal, Pageable pageable);

	@Query(value = Constants.FIND_COLONIES_BY_STATE_NAME_CITY_NAME_COLONY_NAME_CODE_POSTAL, nativeQuery = true)
	Page<Colony> findAllByStateNameAndCityNameAndColonyNameCodePostal(String state, String city, String colony,
			String postalCode, Pageable pageable);
}
