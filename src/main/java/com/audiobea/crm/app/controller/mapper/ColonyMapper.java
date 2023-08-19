package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.business.dao.customer.model.Colony;
import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.utils.Constants;

import org.mapstruct.Mapper;

@Mapper(componentModel = Constants.SPRING)
public interface ColonyMapper {

    DtoInColony colonyToDtoInColony(Colony colony);

    Colony colonyDtoInToColony(DtoInColony dtoInColony);
}
