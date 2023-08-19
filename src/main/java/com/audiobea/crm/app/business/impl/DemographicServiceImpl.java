package com.audiobea.crm.app.business.impl;

import java.util.List;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.audiobea.crm.app.business.IDemographicService;
import com.audiobea.crm.app.business.dao.customer.ICityDao;
import com.audiobea.crm.app.business.dao.customer.IColonyDao;
import com.audiobea.crm.app.business.dao.customer.IStateDao;
import com.audiobea.crm.app.business.dao.customer.model.City;
import com.audiobea.crm.app.business.dao.customer.model.Colony;
import com.audiobea.crm.app.business.dao.customer.model.State;
import com.audiobea.crm.app.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DemographicServiceImpl implements IDemographicService {

    @Autowired
    private IStateDao stateDao;

    @Autowired
    private ICityDao cityDao;

    @Autowired
    private IColonyDao colonyDao;

    @Override
    public List<State> getStates() {
        return StreamSupport.stream(stateDao.findAll(Sort.by("name")).spliterator(), false).toList();
    }

    @Override
    public Page<City> getCitiesByStateId(Long stateId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        return cityDao.findByStateId(stateId, pageable);
    }
    
    @Override
	public Page<City> getAllCities(String state, String city, Integer page, Integer pageSize) {
    	Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Constants.CITY_NAME));
    	log.debug("Ingresamos al servicio");
    	return cityDao.findByStateNameAndCityName(state, city, pageable);
	}

    @Override
    public Page<Colony> findColoniesByStateIdAndCityId(Long stateId, Long cityId, String postalCode, Integer page, Integer pageSize) {
        log.debug("StateId: {}, cityId: {}, postalCode: {}", stateId, cityId, postalCode);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        if (StringUtils.isNotBlank(postalCode)) {
            return colonyDao.findByStateIdAndCityAndCodePostalId(stateId, cityId, postalCode, pageable);
        }
        return colonyDao.findByStateIdAndCityId(stateId, cityId, pageable);
    }

	@Override
	public Page<Colony> getAllColonies(String state, String city, String colony, String codePostal, Integer page, Integer pageSize) {
		log.debug("State: {}, city: {}, postalCode: {}", state, city, codePostal);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
		return colonyDao.findAllByStateNameAndCityNameAndColonyNameCodePostal(state, city, colony, codePostal, pageable);
	}

}
