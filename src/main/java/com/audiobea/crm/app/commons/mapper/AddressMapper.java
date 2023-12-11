package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInAddress;
import com.audiobea.crm.app.dao.demographic.model.Address;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = Constants.SPRING)
public interface AddressMapper {

    DtoInAddress addressToDtoInAddress(Address address);

    @Mapping(target = "state", ignore = true)
    Address addressDtoInToAddress(DtoInAddress dtoBrands);
}
