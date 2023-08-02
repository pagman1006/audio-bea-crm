package com.audiobea.crm.app.business;

import com.audiobea.crm.app.dao.customer.model.Customer;
import org.springframework.data.domain.Page;

public interface ICustomerService {

    Page<Customer> getCustomers(String firstName, String firstLastName, Integer page, Integer pageSize);

    Customer saveCustomer(Customer customer);

    Customer updateCustomer(Long customerId, Customer customer);

    boolean deleteCustomer(Long customerId);

    Customer getCustomerById(Long customerId);
}
