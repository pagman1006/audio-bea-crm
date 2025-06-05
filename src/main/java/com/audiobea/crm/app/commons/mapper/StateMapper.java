package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInState;
import com.audiobea.crm.app.dao.demographic.model.State;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = Constants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StateMapper {

    DtoInState stateToDtoInState(State state);

    @Mapping(target = "cities", ignore = true)
    State dtoIntStateToState(DtoInState dtoState);
}
