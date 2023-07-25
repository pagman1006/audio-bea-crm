package com.audiobea.crm.app.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.City;

public interface ICityDao extends PagingAndSortingRepository<City, Long> {
}
