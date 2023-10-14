package com.audiobea.crm.app.business.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.audiobea.crm.app.business.IDemographicService;
import com.audiobea.crm.app.dao.demographic.ICityDao;
import com.audiobea.crm.app.dao.demographic.IColonyDao;
import com.audiobea.crm.app.dao.demographic.IStateDao;
import com.audiobea.crm.app.dao.demographic.model.City;
import com.audiobea.crm.app.dao.demographic.model.Colony;
import com.audiobea.crm.app.dao.demographic.model.State;
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
		return StreamSupport.stream(stateDao.findAll(Sort.by("name")).spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public Page<City> getCitiesByStateId(String stateId, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
		log.debug("StateId: " + stateId);
		if (StringUtils.isNotBlank(stateId)) {
			if (stateId.equalsIgnoreCase(Constants.ALL)) {
				log.debug("Return All Cities");
				return cityDao.findAll(pageable);
			} else if (stateId.chars().allMatch(Character::isDigit)) {
				log.debug("Return Cities from state id: " + stateId);
				return cityDao.findByStateId(Long.valueOf(stateId), pageable);
			}
		}
		return null;
	}

	@Override
	public Page<City> getAllCities(String state, String city, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Constants.CITY_NAME));
		log.debug("Ingresamos al servicio");
		return cityDao.findByStateNameAndCityName(state, city, pageable);
	}

	@Override
	public Page<Colony> findColoniesByStateIdAndCityId(String stateId, String cityId, String colonyName, String postalCode, Integer page,
			Integer pageSize) {
		log.debug("StateId: {}, cityId: {}, colonyName: {}, postalCode: {}", stateId, cityId, colonyName, postalCode);
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
		postalCode = StringUtils.isNotBlank(postalCode) ? postalCode : "";
		colonyName = StringUtils.isNotBlank(colonyName) ? colonyName : "";

		if (StringUtils.isNotBlank(stateId) && StringUtils.isNotBlank(cityId)) {
			if (stateId.equalsIgnoreCase(Constants.ALL)) {
				if (cityId.equalsIgnoreCase(Constants.ALL)) {
					log.debug("State: All, City: All, Colony: " + colonyName + " Postal Code: " + postalCode);
					return colonyDao.findAllByColonyOrPostalCode(colonyName, postalCode, pageable);

				} else if (cityId.chars().allMatch(Character::isDigit)) {
					log.debug("State: All, City: " + cityId + "Colony: " + colonyName + " Postal Code: " + postalCode);
					return colonyDao.findByCityId(Long.valueOf(cityId), colonyName, postalCode, pageable);
				}
			} else if (stateId.chars().allMatch(Character::isDigit)) {
				if (cityId.equalsIgnoreCase(Constants.ALL)) {
					log.debug("State: " + stateId + " City: All, Colony: " + colonyName + " Postal Code: " + postalCode);
					return colonyDao.findByStateId(Long.valueOf(stateId), colonyName, postalCode, pageable);
				} else if (cityId.chars().allMatch(Character::isDigit)) {
					log.debug("State: " + stateId + " City: " + cityId + "Colony: " + colonyName + " Postal Code: " + postalCode);
					return colonyDao.findByStateIdAndCityAndCodePostalId(Long.valueOf(stateId), Long.valueOf(cityId), colonyName,
							postalCode, pageable);
				}
			}
		}
		log.debug("return null");
		return null;
	}

	@Override
	public Page<Colony> getAllColonies(String state, String city, String colony, String codePostal, Integer page, Integer pageSize) {
		log.debug("State: {}, city: {}, postalCode: {}", state, city, codePostal);
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
		return colonyDao.findAllByStateNameAndCityNameAndColonyNameCodePostal(state, city, colony, codePostal, pageable);
	}

}
