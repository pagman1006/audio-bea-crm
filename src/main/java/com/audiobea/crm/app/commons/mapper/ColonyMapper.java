package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.dao.demographic.model.Colony;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;

@Mapper(componentModel = Constants.SPRING)
public interface ColonyMapper {

    DtoInColony colonyToDtoInColony(Colony colony);

    Colony colonyDtoInToColony(DtoInColony dtoInColony);
}
