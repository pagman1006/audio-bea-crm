package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import com.audiobea.crm.app.dao.customer.model.Customer;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;

@Mapper(componentModel = Constants.SPRING, uses = { UserMapper.class, PhoneMapper.class, EmailMapper.class, ProductMapper.class })
public interface CustomerMapper {

	DtoInCustomer customerToDtoInCustomer(Customer customer);

	Customer customerDtoInToCustomer(DtoInCustomer customer);

}
