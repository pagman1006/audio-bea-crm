package com.audiobea.crm.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.model.customer.Customer;

public interface ICustomerDao extends CrudRepository<Customer, Long> {

}
