package com.audiobea.crm.app.dao.demographic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.demographic.model.City;
import com.audiobea.crm.app.utils.Constants;

public interface ICityDao extends PagingAndSortingRepository<City, Long> {

	City findByName(String name);

	@Query(value = Constants.FIND_CITIES_BY_STATE_ID, nativeQuery = true)
	Page<City> findByStateId(Long stateId, Pageable pageable);

	@Query(value = Constants.FIND_CITIES_BY_STATE_NAME_CITY_NAME, nativeQuery = true)
	Page<City> findByStateNameAndCityName(String state, String city, Pageable pageable);

}