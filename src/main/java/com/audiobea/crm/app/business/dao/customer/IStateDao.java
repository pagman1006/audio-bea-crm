package com.audiobea.crm.app.business.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.customer.model.State;

public interface IStateDao extends PagingAndSortingRepository<State, Long> {

    State findByName(String name);
}
