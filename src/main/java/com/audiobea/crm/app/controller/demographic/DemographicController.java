package com.audiobea.crm.app.controller.demographic;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audiobea.crm.app.business.IDemographicService;
import com.audiobea.crm.app.business.dao.customer.model.City;
import com.audiobea.crm.app.business.dao.customer.model.Colony;
import com.audiobea.crm.app.business.dao.customer.model.State;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCity;
import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.commons.dto.DtoInState;
import com.audiobea.crm.app.controller.mapper.ListCityMapper;
import com.audiobea.crm.app.controller.mapper.ListColonyMapper;
import com.audiobea.crm.app.controller.mapper.ListStateMapper;
import com.audiobea.crm.app.utils.Validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/v1/audio-bea/demographics")
public class DemographicController {

    private final MessageSource messageSource;

    @Autowired
    private IDemographicService demographicService;

    @Autowired
    private ListStateMapper listStateMapper;

    @Autowired
    private ListCityMapper listCityMapper;

    @Autowired
    private ListColonyMapper listColonyMapper;

    
    @GetMapping("/states")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInState>> getStates() {
        List<State> list = demographicService.getStates();
        Validator.validateList(list, messageSource);
        ResponseData<DtoInState> response = new ResponseData<>();
        response.setData(listStateMapper.statesToDtoInStateList(list));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/states/{stateId}/cities")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInCity>> getCitiesByState(@PathVariable(name = "stateId") Long stateId,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        log.debug("State Id: {}", stateId);
        Page<City> pageable = demographicService.getCitiesByStateId(stateId, page, pageSize);
        Validator.validatePage(pageable, messageSource);
        List<DtoInCity> listCities = new ArrayList<>(listCityMapper.citiesToDtoInCities(pageable.getContent()));
        ResponseData<DtoInCity> response = new ResponseData<>(listCities,
                pageable.getNumber(), pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/cities")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInCity>> getAllCities(@RequestParam(name = "state", defaultValue = "", required = false) String state, 
    		@RequestParam(name = "city", defaultValue = "", required = false) String city,
    		@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
    	log.debug("Entramos al controller");
        Page<City> pageable = demographicService.getAllCities(state, city, page, pageSize);
        log.debug("Se encontraron: {} registros", pageable.getContent().size());
        
        Validator.validatePage(pageable, messageSource);
        List<DtoInCity> listCities = new ArrayList<>(listCityMapper.citiesToDtoInCities(pageable.getContent()));
        ResponseData<DtoInCity> response = new ResponseData<>(listCities,
                pageable.getNumber(), pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/states/{stateId}/cities/{cityId}/colonies")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInColony>> getColonies(@PathVariable(name = "stateId") Long stateId,
            @PathVariable(name = "cityId") Long cityId, @RequestParam(name = "postalCode", required = false) String postalCode,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

        log.debug("StateId: {}, cityId: {}", stateId, cityId);
        Page<Colony> pageable = demographicService.findColoniesByStateIdAndCityId(stateId, cityId, postalCode, page, pageSize);
        log.debug("Colonias: {}", pageable.getContent().size());

        Validator.validatePage(pageable, messageSource);
        List<DtoInColony> listColonies = listColonyMapper.colonyToDtoInColony(pageable.getContent());
        ResponseData<DtoInColony> response = new ResponseData<>(listColonies, pageable.getNumber(),
                pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/colonies")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInColony>> getAllColonies(@RequestParam(name = "state", defaultValue = "", required = false) String state,
    		@RequestParam(name = "city", defaultValue = "", required = false) String city, 
    		@RequestParam(name = "colony", defaultValue = "", required = false) String colony, 
    		@RequestParam(name = "postalCode", defaultValue = "", required = false) String postalCode,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

        log.debug("StateI: {}, city: {}, colony: {}, postalCode: {}", state, city, colony, postalCode);
        Page<Colony> pageable = demographicService.getAllColonies(state, city, colony, postalCode, page, pageSize);
        log.debug("Colonias: {}", pageable.getContent().size());

        Validator.validatePage(pageable, messageSource);
        List<DtoInColony> listColonies = listColonyMapper.colonyToDtoInColony(pageable.getContent());
        ResponseData<DtoInColony> response = new ResponseData<>(listColonies, pageable.getNumber(),
                pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
