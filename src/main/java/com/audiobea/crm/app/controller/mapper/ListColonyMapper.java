package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.business.dao.customer.model.Colony;
import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.utils.Constants;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = Constants.SPRING)
public interface ListColonyMapper {

    List<DtoInColony> colonyToDtoInColony(List<Colony> colony);

    List<Colony> colonyDtoInToColony(List<DtoInColony> dtoInColony);
}
