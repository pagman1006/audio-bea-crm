package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInCity;
import com.audiobea.crm.app.dao.demographic.model.City;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = Constants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityMapper {

    DtoInCity cityToDtoInCity(City city);

    City dtoIntCityToCity(DtoInCity city);
}
