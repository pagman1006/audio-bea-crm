package com.audiobea.crm.app.controller.mapper;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import com.audiobea.crm.app.dao.customer.model.Customer;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING, uses = { PhoneMapper.class, EmailMapper.class })
public interface CustomerMapper {

	DtoInCustomer customerToDtoInCustomer(Customer customer);

	Customer customerDtoInToCustomer(DtoInCustomer customer);

}
