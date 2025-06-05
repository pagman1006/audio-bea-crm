package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCity;
import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.commons.dto.DtoInState;

public interface IDemographicService {

    ResponseData<DtoInState> getStates();

    DtoInState getStateById(String stateId);

    ResponseData<DtoInCity> getCitiesByStateId(String stateId, String cityName, Integer page, Integer pageSize);

    ResponseData<DtoInColony> findColoniesByStateIdAndCityId(String stateId, String cityId, String colonyName, String postalCode, Integer page, Integer pageSize);

    ResponseData<DtoInColony> getAllColonies(String state, String city, String colony, String postalCode, Integer page, Integer pageSize);
}
