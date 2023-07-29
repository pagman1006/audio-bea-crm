package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.dao.customer.model.Colony;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ListColonyMapper {

    List<DtoInColony> colonyToDtoInColony(List<Colony> colony);

    List<Colony> colonyDtoInToColony(List<DtoInColony> dtoInColony);
}
