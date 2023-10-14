package com.audiobea.crm.app.business;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import com.audiobea.crm.app.dao.customer.model.Customer;

public interface ICustomerService {

	Page<Customer> getCustomers(String firstName, String firstLastName, Integer page, Integer pageSize);

	Customer saveCustomer(Customer customer);

	Customer updateCustomer(Long customerId, Customer customer);

	boolean deleteCustomer(Long customerId);

	Customer getCustomerById(Long customerId, Authentication auth);
}
