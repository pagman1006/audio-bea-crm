package com.audiobea.crm.app.business;

import java.util.List;

import org.springframework.data.domain.Page;

import com.audiobea.crm.app.dao.customer.model.City;
import com.audiobea.crm.app.dao.customer.model.Colony;
import com.audiobea.crm.app.dao.customer.model.State;

public interface IDemographicService {

    List<State> getStates();
    Page<City> getAllCities(String state, String city, Integer page, Integer pageSize);
    Page<City> getCitiesByStateId(Long stateId, Integer page, Integer pageSize);
    Page<Colony> findColoniesByStateIdAndCityId(Long stateId, Long cityId, String postalCode, Integer page, Integer pageSize);
	Page<Colony> getAllColonies(String state, String city, String colony, String codePostal, Integer page, Integer pageSize);
}
