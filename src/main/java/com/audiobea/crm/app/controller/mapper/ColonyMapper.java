package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.dao.customer.model.Colony;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColonyMapper {

    DtoInColony colonyToDtoInColony(Colony colony);

    Colony colonyDtoInToColony(DtoInColony dtoInColony);
}
