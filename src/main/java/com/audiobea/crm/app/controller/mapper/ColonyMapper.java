package com.audiobea.crm.app.controller.mapper;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.business.dao.demographic.model.Colony;
import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface ColonyMapper {

    DtoInColony colonyToDtoInColony(Colony colony);

    Colony colonyDtoInToColony(DtoInColony dtoInColony);
}
