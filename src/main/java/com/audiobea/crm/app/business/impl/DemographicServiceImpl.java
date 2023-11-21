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
import java.util.stream.Collectors;

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
        List<State> listStates = stateDao.findAll();
        Validator.validateList(listStates, messageSource);
        ResponseData<DtoInState> response = new ResponseData<>();
        response.setData(
                listStates.stream().map(state -> stateMapper.stateToDtoInState(state)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public DtoInState getStateById(String stateId) {
        return stateMapper.stateToDtoInState(
                stateDao.findById(stateId).orElseThrow(() -> new NoSuchElementFoundException(
                        Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), stateId))));
    }

    @Override
    public ResponseData<DtoInCity> getCitiesByStateId(String stateId, String cityName, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        log.debug("StateId: " + stateId);
        Page<City> pageCities = null;
        if (StringUtils.isNotBlank(stateId)) {
            if (stateId.equalsIgnoreCase(Constants.ALL)) {
                log.debug("City: {}", cityName);
                pageCities = StringUtils.isBlank(cityName) ? cityDao.findAll(pageable)
                        : cityDao.findByNameContaining(cityName.toUpperCase(), pageable);
            } else {
                log.debug("Return Cities from state id: {}, cityName: {}", stateId, cityName.toUpperCase());
                pageCities = StringUtils.isBlank(cityName) ? cityDao.findByStateId(stateId, pageable)
                        : cityDao.findByStateIdAndName(stateId, cityName.toUpperCase(), pageable);
            }
        }
        Validator.validatePage(pageCities, messageSource);
        return new ResponseData<>(pageCities.getContent().stream().map(city -> cityMapper.cityToDtoInCity(city))
                                            .collect(Collectors.toList()), pageCities);
    }

    @Override
    public ResponseData<DtoInColony> findColoniesByStateIdAndCityId(String stateId, String cityId, String colonyName,
            String postalCode, Integer page, Integer pageSize)
    {
        log.debug("StateId: {}, cityId: {}, colonyName: {}, postalCode: {}", stateId, cityId, colonyName, postalCode);
        if (StringUtils.isBlank(stateId) || StringUtils.isBlank(cityId)) {
            throw new IllegalArgumentException(
                    Utils.getLocalMessage(messageSource, I18Constants.INVALID_PARAMETERS.getKey(), stateId, cityId));
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        Page<Colony> pageColonies;
        if (stateId.equalsIgnoreCase(Constants.ALL)) {
            pageColonies = cityId.equalsIgnoreCase(Constants.ALL) ?
                    colonyDao.findAllByColonyOrPostalCode(colonyName, postalCode, pageable)
                    : colonyDao.findByCityId(cityId, colonyName, postalCode, pageable);
        } else {
            pageColonies = cityId.equalsIgnoreCase(Constants.ALL) ?
                    colonyDao.findByStateId(stateId, colonyName, postalCode, pageable)
                    : colonyDao.findByStateIdAndCityId(stateId, cityId, colonyName, postalCode, pageable);
        }
        Validator.validatePage(pageColonies, messageSource);
        return new ResponseData<>(
                pageColonies.getContent().stream().map(colony -> colonyMapper.colonyToDtoInColony(colony))
                            .collect(Collectors.toList()), pageColonies);
    }

    @Override
    public ResponseData<DtoInColony> getAllColonies(String stateName, String cityName, String colonyName,
            String postalCode, Integer page, Integer pageSize)
    {
        log.debug("State: {}, city: {}, colony: {}, postalCode: {}", stateName, cityName, colonyName, postalCode);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        Page<Colony> pageColonies = null;
        if (StringUtils.isBlank(stateName) && StringUtils.isBlank(cityName)) {
            log.debug("State blank and city blank");
            pageColonies = colonyDao.findAllByColonyOrPostalCode(colonyName, postalCode, pageable);
        } else if (StringUtils.isNotBlank(stateName)) {
            log.debug("State: {}", stateName);
            List<State> states = stateDao.findAllByName(stateName.toUpperCase());
            Validator.validateList(states, messageSource);
            pageColonies = colonyDao.findByStateIdInAndNameContainingAndPostalCodeContaining(
                    states.stream().map(State::getId).toList(), colonyName,
                    postalCode, pageable);
        } else if (StringUtils.isNotBlank(cityName)) {
            log.debug("City: {}", cityName);
            List<City> cities = cityDao.findAllByName(cityName.toUpperCase());
            Validator.validateList(cities, messageSource);
            pageColonies = colonyDao.findByCityIdInAndNameContainingAndPostalCodeContaining(
                    cities.stream().map(City::getId).toList(), colonyName, postalCode, pageable);
        } else {
            log.debug("State: {}, city: {}", stateName, cityName);
            List<State> states = stateDao.findAllByName(stateName.toUpperCase());
            Validator.validateList(states, messageSource);
            List<City> cities = cityDao.findAllByName(cityName.toUpperCase());
            Validator.validateList(cities, messageSource);
            pageColonies = colonyDao.findByStateIdInAndCityIdInAndNameContainingAndPostalCodeContaining(
                    states.stream().map(State::getId).toList(), cities.stream().map(City::getId).toList(),
                    colonyName, postalCode, pageable);
        }
        Validator.validatePage(pageColonies, messageSource);
        return new ResponseData<>(pageColonies.getContent()
                                              .stream()
                                              .map(c -> colonyMapper.colonyToDtoInColony(c))
                                              .collect(Collectors.toList()), pageColonies);
    }
}
