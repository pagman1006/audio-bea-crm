package com.audiobea.crm.app.business.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.customer.model.Address;

public interface IAddressDao extends PagingAndSortingRepository<Address, Long> {
}
