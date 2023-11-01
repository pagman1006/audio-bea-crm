package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.IDemographicService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCity;
import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.commons.dto.DtoInState;
import com.audiobea.crm.app.commons.mapper.CityMapper;
import com.audiobea.crm.app.commons.mapper.ColonyMapper;
import com.audiobea.crm.app.commons.mapper.StateMapper;
import com.audiobea.crm.app.dao.demographic.ICityDao;
import com.audiobea.crm.app.dao.demographic.IColonyDao;
import com.audiobea.crm.app.dao.demographic.IStateDao;
import com.audiobea.crm.app.dao.demographic.model.City;
import com.audiobea.crm.app.dao.demographic.model.Colony;
import com.audiobea.crm.app.dao.demographic.model.State;
import com.audiobea.crm.app.utils.Constants;
import com.audiobea.crm.app.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        List<State> listStates = StreamSupport.stream(stateDao.findAll(Sort.by("name")).spliterator(), false).collect(Collectors.toList());
        Validator.validateList(listStates, messageSource);
        ResponseData<DtoInState> response = new ResponseData<>();
        response.setData(listStates.stream().map(state -> stateMapper.stateToDtoInState(state)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public ResponseData<DtoInCity> getCitiesByStateId(String stateId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        log.debug("StateId: " + stateId);
        Page<City> pageCities = null;
        if (StringUtils.isNotBlank(stateId)) {
            if (stateId.equalsIgnoreCase(Constants.ALL)) {
                log.debug("Return All Cities");
                pageCities = cityDao.findAll(pageable);
            } else if (stateId.chars().allMatch(Character::isDigit)) {
                log.debug("Return Cities from state id: " + stateId);
                pageCities = cityDao.findByStateId(Long.valueOf(stateId), pageable);
            }
        }
        Validator.validatePage(pageCities, messageSource);
        return new ResponseData<>(pageCities.getContent().stream().map(city -> cityMapper.stateToDtoInState(city)).collect(Collectors.toList()), pageCities);
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

                } else if (cityId.chars().allMatch(Character::isDigit)) {
                    log.debug("State: All, City: " + cityId + "Colony: " + colonyName + " Postal Code: " + postalCode);
                    pageColonies = colonyDao.findByCityId(Long.valueOf(cityId), colonyName, postalCode, pageable);
                }
            } else if (stateId.chars().allMatch(Character::isDigit)) {
                if (cityId.equalsIgnoreCase(Constants.ALL)) {
                    log.debug("State: " + stateId + " City: All, Colony: " + colonyName + " Postal Code: " + postalCode);
                    pageColonies = colonyDao.findByStateId(Long.valueOf(stateId), colonyName, postalCode, pageable);
                } else if (cityId.chars().allMatch(Character::isDigit)) {
                    log.debug("State: " + stateId + " City: " + cityId + "Colony: " + colonyName + " Postal Code: " + postalCode);
                    pageColonies = colonyDao.findByStateIdAndCityAndCodePostalId(Long.valueOf(stateId), Long.valueOf(cityId), colonyName, postalCode, pageable);
                }
            }
        }
        Validator.validatePage(pageColonies, messageSource);
        return new ResponseData<>(pageColonies.getContent().stream().map(colony -> colonyMapper.colonyToDtoInColony(colony)).collect(Collectors.toList()), pageColonies);
    }

    @Override
    public ResponseData<DtoInColony> getAllColonies(String state, String city, String colony, String codePostal, Integer page, Integer pageSize) {
        log.debug("State: {}, city: {}, postalCode: {}", state, city, codePostal);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
        Page<Colony> pageColonies = colonyDao.findAllByStateNameAndCityNameAndColonyNameCodePostal(state, city, colony, codePostal, pageable);
        Validator.validatePage(pageColonies, messageSource);
        return new ResponseData<>(pageColonies.getContent().stream().map(c -> colonyMapper.colonyToDtoInColony(c)).collect(Collectors.toList()), pageColonies);
    }

}
