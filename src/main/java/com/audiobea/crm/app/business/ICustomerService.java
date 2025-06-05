package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import org.springframework.security.core.Authentication;

public interface ICustomerService {

	ResponseData<DtoInCustomer> getCustomers(String name, String lastName, Integer page, Integer pageSize);

	DtoInCustomer saveCustomer(DtoInCustomer customer);

	DtoInCustomer updateCustomer(String customerId, DtoInCustomer customer);

	void deleteCustomer(String customerId);

	DtoInCustomer getCustomerById(String customerId, Authentication auth);
}
