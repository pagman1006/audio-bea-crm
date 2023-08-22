package com.audiobea.crm.app.business.dao.demographic;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.demographic.model.Address;

public interface IAddressDao extends PagingAndSortingRepository<Address, Long> {
}
