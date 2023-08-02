package com.audiobea.crm.app.business;

import java.util.List;

import org.springframework.data.domain.Page;

import com.audiobea.crm.app.dao.customer.model.City;
import com.audiobea.crm.app.dao.customer.model.Colony;
import com.audiobea.crm.app.dao.customer.model.State;

public interface IDemographicService {

    List<State> getStates();
    Page<City> getCitiesByStateId(Long stateId, Integer page, Integer pageSize);
    Page<Colony> findColoniesByStateIdAndCityId(Long stateId, Long cityId, String postalCode, Integer page, Integer pageSize);
	Page<Colony> findColonies(Long stateId, Long cityId, String codePostal, Integer page, Integer pageSize);
}
