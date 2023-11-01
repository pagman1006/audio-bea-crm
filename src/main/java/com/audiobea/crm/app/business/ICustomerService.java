package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import com.audiobea.crm.app.dao.customer.model.Customer;

public interface ICustomerService {

	ResponseData<DtoInCustomer> getCustomers(String firstName, String firstLastName, Integer page, Integer pageSize);

	DtoInCustomer saveCustomer(DtoInCustomer customer);

	DtoInCustomer updateCustomer(Long customerId, DtoInCustomer customer);

	boolean deleteCustomer(Long customerId);

	DtoInCustomer getCustomerById(Long customerId, Authentication auth);
}
