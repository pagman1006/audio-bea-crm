package com.audiobea.crm.app.dao.customer;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.customer.model.Customer;

public interface ICustomerDao extends CrudRepository<Customer, Long> {

}
