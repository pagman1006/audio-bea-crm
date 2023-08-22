package com.audiobea.crm.app.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.audiobea.crm.app.business.dao.demographic.model.City;
import com.audiobea.crm.app.commons.dto.DtoInCity;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListCityMapper {

    List<DtoInCity> citiesToDtoInCities(List<City> cities);

    @Mapping(target = "colonies", ignore = true)
    List<City> citiesDtoInToCities(List<DtoInCity> dtoInCities);
}
