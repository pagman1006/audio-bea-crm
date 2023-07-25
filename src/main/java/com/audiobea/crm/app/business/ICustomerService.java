package com.audiobea.crm.app.business;

import org.springframework.data.domain.Page;

import com.audiobea.crm.app.dao.customer.model.Customer;

public interface ICustomerService {
	
	public Page<Customer> getCustomers(String firstName, String secondName, String firstLastName, String secondLastName, Integer page, Integer pageSize);
	public Customer saveCustomer(Customer customer);
	public Customer updateCustomer(Long customerId, Customer customer);
	public boolean deleteCustomer(Long customerId);
	public Customer getCustomerById(Long customerId);
}
