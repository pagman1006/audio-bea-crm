package com.audiobea.crm.app.business;

import java.util.List;

import org.springframework.data.domain.Page;

import com.audiobea.crm.app.business.dao.demographic.model.City;
import com.audiobea.crm.app.business.dao.demographic.model.Colony;
import com.audiobea.crm.app.business.dao.demographic.model.State;

public interface IDemographicService {

	List<State> getStates();

	Page<City> getAllCities(String state, String city, Integer page, Integer pageSize);

	Page<City> getCitiesByStateId(String stateId, Integer page, Integer pageSize);

	Page<Colony> findColoniesByStateIdAndCityId(String stateId, String cityId, String colonyName, String postalCode, Integer page,
			Integer pageSize);

	Page<Colony> getAllColonies(String state, String city, String colony, String codePostal, Integer page, Integer pageSize);
}
