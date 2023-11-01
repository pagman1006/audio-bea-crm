package com.audiobea.crm.app.commons.mapper;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.dao.demographic.model.Colony;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface ColonyMapper {

    DtoInColony colonyToDtoInColony(Colony colony);

    Colony colonyDtoInToColony(DtoInColony dtoInColony);
}
