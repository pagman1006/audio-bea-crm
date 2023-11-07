package com.audiobea.crm.app.dao.demographic;

import com.audiobea.crm.app.dao.demographic.model.City;
import com.audiobea.crm.app.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ICityDao extends PagingAndSortingRepository<City, Long> {

	City findByName(String name);

	@Query(value = Constants.FIND_CITIES_BY_STATE_ID, nativeQuery = true)
	Page<City> findByStateId(@Param("stateId") Long stateId, Pageable pageable);

	@Query(value = Constants.FIND_CITIES_BY_STATE_NAME_CITY_NAME, nativeQuery = true)
	Page<City> findByStateNameAndCityName(@Param("state") String state, @Param("city") String city, Pageable pageable);

}