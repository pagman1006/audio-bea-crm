package com.audiobea.crm.app.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import com.audiobea.crm.app.dao.customer.model.Customer;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING, uses = CustomerMapper.class)
public interface ListCustomerMapper {

	List<DtoInCustomer> listCustomersToListDtoInCustomers(List<Customer> customers);

	List<Customer> listCustomersDtoInToListCustomers(List<DtoInCustomer> customers);

}
