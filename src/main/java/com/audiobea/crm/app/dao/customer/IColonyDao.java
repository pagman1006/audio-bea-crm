package com.audiobea.crm.app.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.Colony;

public interface IColonyDao extends PagingAndSortingRepository<Colony, Long> {
	
//	Page<Colony> findByPostalCodeContains(String postalCode, Pageable pageable);
//	Page<Colony> findByStateNameContainsAndCityNameContains(String stateName, String cityName, Pageable pageable);
//	Page<Colony> findByStateNameContains(String stateName, Pageable pageable);
}
