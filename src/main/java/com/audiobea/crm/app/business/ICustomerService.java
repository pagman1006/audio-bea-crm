package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import org.springframework.security.core.Authentication;

public interface ICustomerService {

	ResponseData<DtoInCustomer> getCustomers(String firstName, String firstLastName, Integer page, Integer pageSize);

	DtoInCustomer saveCustomer(DtoInCustomer customer);

	DtoInCustomer updateCustomer(Long customerId, DtoInCustomer customer);

	void deleteCustomer(Long customerId);

	DtoInCustomer getCustomerById(Long customerId, Authentication auth);
}
