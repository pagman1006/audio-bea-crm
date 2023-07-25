package com.audiobea.crm.app.dao.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.Customer;

public interface ICustomerDao extends PagingAndSortingRepository<Customer, Long> {

	Page<Customer> findByFirstNameContainsAndSecondNameContainsAndFirstLastNameContainsAndSecondLastNameContains(
			String firstName, String secondName, String firstLastName, String secondLastName, Pageable pageable);

}
