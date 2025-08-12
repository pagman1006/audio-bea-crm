package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.IDemographicService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCity;
import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.commons.dto.DtoInState;
import com.audiobea.crm.app.commons.mapper.CityMapper;
import com.audiobea.crm.app.commons.mapper.ColonyMapper;
import com.audiobea.crm.app.commons.mapper.StateMapper;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.dao.demographic.ICityDao;
import com.audiobea.crm.app.dao.demographic.IColonyDao;
import com.audiobea.crm.app.dao.demographic.IStateDao;
import com.audiobea.crm.app.dao.demographic.model.City;
import com.audiobea.crm.app.dao.demographic.model.Colony;
import com.audiobea.crm.app.dao.demographic.model.State;
import com.audiobea.crm.app.utils.Constants;
import com.audiobea.crm.app.utils.Utils;
import com.audiobea.crm.app.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.audiobea.crm.app.utils.ConstantsLog.*;

@Slf4j
@AllArgsConstructor
@Service
public class DemographicServiceImpl implements IDemographicService {

    private MessageSource messageSource;
    private IStateDao stateDao;
    private ICityDao cityDao;
    private IColonyDao colonyDao;

    private StateMapper stateMapper;
    private CityMapper cityMapper;
    private ColonyMapper colonyMapper;

    @Override
    public ResponseData<DtoInState> getStates() {
        final List<State> listStates = stateDao.findAll();
        Validator.validateList(listStates, messageSource);
        final ResponseData<DtoInState> response = new ResponseData<>();
        response.setData(listStates.stream().map(stateMapper::stateToDtoInState).toList());
        return response;
    }

    @Override
    public DtoInState getStateById(final String stateId) {
        return stateMapper.stateToDtoInState(
                stateDao.findById(stateId).orElseThrow(() -> new NoSuchElementFoundException(
                        Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), stateId))));
    }

    @Override
    public ResponseData<DtoInCity> getCitiesByStateId(final String stateId, final String cityName, final Integer page,
            final Integer pageSize) {
        final Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        log.debug(LOG_STATE, stateId);
        Page<City> pageCities = null;
        if (StringUtils.isNotBlank(stateId)) {
            if (stateId.equalsIgnoreCase(Constants.ALL)) {
                log.debug(LOG_CITY, cityName);
                pageCities = StringUtils.isBlank(cityName) ? cityDao.findAll(pageable)
                        : cityDao.findByNameContaining(cityName.toUpperCase(), pageable);
            } else {
                log.debug(LOG_CITIES_FROM_STATE, stateId, cityName.toUpperCase());
                pageCities = StringUtils.isBlank(cityName) ? cityDao.findByStateId(stateId, pageable)
                        : cityDao.findByStateIdAndName(stateId, cityName.toUpperCase(), pageable);
            }
        }
        Validator.validatePage(pageCities, messageSource);
        return new ResponseData<>(
                pageCities.getContent().stream().map(cityMapper::cityToDtoInCity).peek(c -> c.setColonies(null))
                        .toList(), pageCities);
    }

    @Override
    public ResponseData<DtoInColony> findColoniesByStateIdAndCityId(final String stateId, final String cityId,
            String colonyName, String postalCode, final Integer page, final Integer pageSize) {
        log.debug(LOG_STATE_CITY_COLONY_POSTAL_CODE, stateId, cityId, colonyName, postalCode);
        if (StringUtils.isBlank(stateId) || StringUtils.isBlank(cityId)) {
            throw new IllegalArgumentException(
                    Utils.getLocalMessage(messageSource, I18Constants.INVALID_PARAMETERS.getKey(), stateId, cityId));
        }
        final Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        postalCode = StringUtils.isNotBlank(postalCode) ? postalCode : "";
        colonyName = StringUtils.isNotBlank(colonyName) ? colonyName : "";
        Page<Colony> pageColonies = null;
        if (StringUtils.isNotBlank(stateId) && StringUtils.isNotBlank(cityId)) {
            if (stateId.equalsIgnoreCase(Constants.ALL)) {
                if (cityId.equalsIgnoreCase(Constants.ALL)) {
                    log.debug(LOG_STATE_ALL_CITY_ALL_COLONY_POSTAL_CODE, colonyName, postalCode);
                    pageColonies = colonyDao.findAllByColonyOrPostalCode(colonyName, postalCode, pageable);
                } else {
                    pageColonies = colonyDao.findByCityId(cityId, colonyName, postalCode, pageable);
                }
            } else {
                log.debug(LOG_STATE_CITY_ALL_COLONY_POSTAL_CODE, stateId, cityId, colonyName, postalCode);
                pageColonies = colonyDao.findByStateIdAndCityId(stateId, cityId, colonyName, postalCode, pageable);
            }
        }
        Validator.validatePage(pageColonies, messageSource);
        return new ResponseData<>(
                pageColonies.getContent().stream().map(colonyMapper::colonyToDtoInColony).toList(), pageColonies);
    }

    @Override
    public ResponseData<DtoInColony> getAllColonies(final String stateName, final String cityName,
            final String colonyName, final String postalCode, final Integer page, final Integer pageSize) {
        log.debug(LOG_STATE_CITY_ALL_COLONY_POSTAL_CODE, stateName, cityName, colonyName, postalCode);
        final Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        final Page<Colony> pageColonies;
        if (StringUtils.isBlank(stateName) && StringUtils.isBlank(cityName)) {
            log.debug(LOG_STATE_CITY_BLANK);
            pageColonies = colonyDao.findAllByColonyOrPostalCode(colonyName, postalCode, pageable);
        } else if (StringUtils.isNotBlank(stateName)) {
            log.debug(LOG_STATE, stateName);
            final List<State> states = stateDao.findAllByName(stateName.toUpperCase());
            Validator.validateList(states, messageSource);
            pageColonies = colonyDao.findByStateIdInAndNameContainingAndPostalCodeContaining(
                    states.stream().map(State::getId).toList(), colonyName,
                    postalCode, pageable);
        } else if (StringUtils.isNotBlank(cityName)) {
            log.debug(LOG_CITY, cityName);
            final List<City> cities = cityDao.findAllByName(cityName.toUpperCase());
            Validator.validateList(cities, messageSource);
            pageColonies = colonyDao.findByCityIdInAndNameContainingAndPostalCodeContaining(
                    cities.stream().map(City::getId).toList(), colonyName, postalCode, pageable);
        } else {
            log.debug(LOG_STATE_CITY, stateName, cityName);
            final List<State> states = stateDao.findAllByName(stateName.toUpperCase());
            Validator.validateList(states, messageSource);
            final List<City> cities = cityDao.findAllByName(cityName.toUpperCase());
            Validator.validateList(cities, messageSource);
            pageColonies = colonyDao.findByStateIdInAndCityIdInAndNameContainingAndPostalCodeContaining(
                    states.stream().map(State::getId).toList(), cities.stream().map(City::getId).toList(),
                    colonyName, postalCode, pageable);
        }
        Validator.validatePage(pageColonies, messageSource);
        return new ResponseData<>(pageColonies.getContent()
                .stream().map(colonyMapper::colonyToDtoInColony).toList(), pageColonies);
    }
}
