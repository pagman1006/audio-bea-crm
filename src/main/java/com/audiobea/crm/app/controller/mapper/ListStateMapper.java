package com.audiobea.crm.app.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.audiobea.crm.app.business.dao.demographic.model.State;
import com.audiobea.crm.app.commons.dto.DtoInState;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListStateMapper {

    List<DtoInState> statesToDtoInStateList(List<State> states);
    
    @Mapping(ignore = true, target = "cities")
    List<State> dtoIntStatesToStateList(List<DtoInState> dtoStates);
}
