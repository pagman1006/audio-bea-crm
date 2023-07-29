package com.audiobea.crm.app.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.State;

public interface IStateDao extends PagingAndSortingRepository<State, Long> {

    State findByName(String name);
}
