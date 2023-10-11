package com.audiobea.crm.app.controller.mapper;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.business.dao.customer.model.Customer;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING, uses = { UserMapper.class, PhoneMapper.class, EmailMapper.class, ProductMapper.class })
public interface CustomerMapper {

	DtoInCustomer customerToDtoInCustomer(Customer customer);

	Customer customerDtoInToCustomer(DtoInCustomer customer);

}
