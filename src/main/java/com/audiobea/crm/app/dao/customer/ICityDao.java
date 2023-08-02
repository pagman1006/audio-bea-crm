package com.audiobea.crm.app.dao.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.City;

public interface ICityDao extends PagingAndSortingRepository<City, Long> {

    City findByName(String name);
    @Query(value = "SELECT * FROM cities WHERE state_id = :stateId", nativeQuery = true)
    Page<City> findByStateId(Long stateId, Pageable pageable);
}
