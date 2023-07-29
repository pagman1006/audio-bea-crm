package com.audiobea.crm.app.dao.customer;

import com.audiobea.crm.app.dao.customer.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICustomerDao extends PagingAndSortingRepository<Customer, Long> {

    Page<Customer> findByFirstNameContainsOrSecondNameContainsOrFirstLastNameContainsOrSecondLastNameContains(
            String firstName, String secondName, String firstLastName, String secondLastName, Pageable pageable);

}
