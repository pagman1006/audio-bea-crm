package com.audiobea.crm.app.business;

import java.util.List;

import com.audiobea.crm.app.dao.customer.model.Customer;

public interface ICustomerService {
	
	public List<Customer> getCustomer();
	public Customer saveCustomer(Customer customer);
	public Customer updateCustomer(Long customerId, Customer customer);
	public boolean deleteCustomer(Long customerId);

}
