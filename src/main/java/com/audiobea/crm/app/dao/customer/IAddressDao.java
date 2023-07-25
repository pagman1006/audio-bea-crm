package com.audiobea.crm.app.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.Address;

public interface IAddressDao extends PagingAndSortingRepository<Address, Long> {
}
