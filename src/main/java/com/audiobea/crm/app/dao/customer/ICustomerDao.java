package com.audiobea.crm.app.dao.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.Customer;

public interface ICustomerDao extends PagingAndSortingRepository<Customer, Long>, CrudRepository<Customer, Long> {

	Page<Customer> findByFirstNameContains(String firstName, Pageable pageable);

	Page<Customer> findByFirstLastNameContains(String firstLastName, Pageable pageable);

	Page<Customer> findByFirstNameContainsAndFirstLastNameContains(String firstName, String firstLastName, Pageable pageable);

}
