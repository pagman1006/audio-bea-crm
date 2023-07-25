package com.audiobea.crm.app.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.Colony;

public interface IColonyDao extends PagingAndSortingRepository<Colony, Long> {
}
