package com.audiobea.crm.app.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.commons.dto.DtoInState;
import com.audiobea.crm.app.dao.customer.model.State;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface ListStateMapper {

    List<DtoInState> statesToDtoInStateList(List<State> states);
    List<State> dtoIntStatesToStateList(List<DtoInState> dtoStates);
}
