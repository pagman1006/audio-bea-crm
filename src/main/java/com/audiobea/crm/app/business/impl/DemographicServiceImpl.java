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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class DemographicServiceImpl implements IDemographicService {

    private final MessageSource messageSource;
    @Autowired
    private IStateDao stateDao;
    @Autowired
    private ICityDao cityDao;
    @Autowired
    private IColonyDao colonyDao;

    @Autowired
    private StateMapper stateMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private ColonyMapper colonyMapper;

    @Override
    public ResponseData<DtoInState> getStates() {
        List<State> listStates = stateDao.findAll();
        Validator.validateList(listStates, messageSource);
        ResponseData<DtoInState> response = new ResponseData<>();
        response.setData(listStates.stream().map(state -> stateMapper.stateToDtoInState(state)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public DtoInState getStateById(String stateId) {
        return stateMapper.stateToDtoInState(stateDao.findById(stateId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), stateId))));
    }

    @Override
    public ResponseData<DtoInCity> getCitiesByStateId(String stateId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        log.debug("StateId: " + stateId);
        Page<City> pageCities = null;
        if (StringUtils.isNotBlank(stateId)) {
            if (stateId.equalsIgnoreCase(Constants.ALL)) {
                pageCities = cityDao.findAll(pageable);
            } else {
                log.debug("Return Cities from state id: " + stateId);
                State state = stateDao.findById(stateId).orElseThrow(() -> new NoSuchElementFoundException(
                        Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), stateId)));
                if (state != null) {
                    List<String> names = state.getCities().stream().map(City::getId).collect(Collectors.toList());
                    pageCities = cityDao.findByStateIdIn(names, pageable);
                }
            }
        }
        Validator.validatePage(pageCities, messageSource);
        return new ResponseData<>(pageCities.getContent().stream().map(city -> cityMapper.cityToDtoInCity(city))
                .collect(Collectors.toList()), pageCities);
    }

    @Override
    public ResponseData<DtoInColony> findColoniesByStateIdAndCityId(String stateId, String cityId, String colonyName, String postalCode, Integer page, Integer pageSize) {
        log.debug("StateId: {}, cityId: {}, colonyName: {}, postalCode: {}", stateId, cityId, colonyName, postalCode);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        postalCode = StringUtils.isNotBlank(postalCode) ? postalCode : "";
        colonyName = StringUtils.isNotBlank(colonyName) ? colonyName : "";
        Page<Colony> pageColonies = null;
        if (StringUtils.isNotBlank(stateId) && StringUtils.isNotBlank(cityId)) {
            if (stateId.equalsIgnoreCase(Constants.ALL)) {
                if (cityId.equalsIgnoreCase(Constants.ALL)) {
                    log.debug("State: All, City: All, Colony: " + colonyName + " Postal Code: " + postalCode);
                    pageColonies = colonyDao.findAllByColonyOrPostalCode(colonyName, postalCode, pageable);
                } else {
                    City city = cityDao.findById(cityId).orElseThrow(() -> new NoSuchElementFoundException(
                            Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), cityId)));
                    if (city != null) {
                        log.debug("State: All, City: " + cityId + " Colony: " + colonyName + " Postal Code: " + postalCode);
                        List<String> names = city.getColonies().stream().map(Colony::getId).collect(Collectors.toList());
                        log.debug("names size: {}", names.size());
                        pageColonies = colonyDao.findByCityId(names, colonyName, postalCode, pageable);
                        log.debug("Colonies size: {}", pageColonies.getContent().size());
                    }
                }
            } else {
                log.debug("State: {} City: {}, Colony: {}, Postal Code: {}", stateId, cityId, colonyName, postalCode);
                State state = stateDao.findById(stateId).orElseThrow(() -> new NoSuchElementFoundException(
                        Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), stateId)));
                log.debug("State Name: {}", state.getName());
                final List<String> names = new ArrayList<>();
                if (cityId.equalsIgnoreCase(Constants.ALL)) {
                    state.getCities().forEach(city -> city.getColonies().forEach(col -> names.add(col.getId())));
                } else {
                    City city = state.getCities().stream().filter(c -> c.getId().equalsIgnoreCase(cityId)).findFirst()
                            .orElseThrow(() -> new NoSuchElementFoundException(Utils
                                    .getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), stateId)));
                    city.getColonies().forEach(col -> names.add(col.getId()));
                }
                log.debug("Names Size: {}", names.size());
                pageColonies = colonyDao.findByCityId(names, colonyName, postalCode, pageable);
            }
        }
        Validator.validatePage(pageColonies, messageSource);
        return new ResponseData<>(pageColonies.getContent().stream().map(colony -> colonyMapper.colonyToDtoInColony(colony))
                .collect(Collectors.toList()), pageColonies);
    }

    @Override
    public ResponseData<DtoInColony> getAllColonies(String stateName, String cityName, String colonyName, String postalCode, Integer page, Integer pageSize) {
        log.debug("State: {}, city: {}, colony: {}, postalCode: {}", stateName, cityName, colonyName, postalCode);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        Page<Colony> pageColonies;
        List<City> listCities;
        List<String> names = new ArrayList<>();

        if (StringUtils.isNotBlank(stateName)) {
            List<State> listState = stateDao.findAllByName(stateName);
            listState.forEach(state -> state.getCities().forEach(city -> names.add(city.getId())));
            listCities = cityDao.findAllByStateIdIn(names);
            listCities.forEach(city -> city.getColonies().forEach(col -> names.add(col.getId())));
        }
        if (names.isEmpty()) {
            pageColonies = colonyDao.findAllByColonyOrPostalCode(colonyName, postalCode, pageable);
        } else {
            pageColonies = colonyDao.findByCityId(names, colonyName, postalCode, pageable);
        }
        Validator.validatePage(pageColonies, messageSource);
        return new ResponseData<>(pageColonies.getContent().stream().map(c -> colonyMapper.colonyToDtoInColony(c)).collect(Collectors.toList()), pageColonies);
    }
}
