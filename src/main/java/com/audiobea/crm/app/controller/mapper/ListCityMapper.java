package com.audiobea.crm.app.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.audiobea.crm.app.commons.dto.DtoInCity;
import com.audiobea.crm.app.dao.customer.model.City;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface ListCityMapper {

    List<DtoInCity> citiesToDtoInCities(List<City> cities);

    @Mapping(target = "colonies", ignore = true)
    List<City> citiesDtoInToCities(List<DtoInCity> dtoInCities);
}
