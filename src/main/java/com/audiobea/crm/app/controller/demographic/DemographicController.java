package com.audiobea.crm.app.controller.demographic;

import com.audiobea.crm.app.business.IDemographicService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCity;
import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.commons.dto.DtoInState;
import com.audiobea.crm.app.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/demographics")
public class DemographicController {

    private final MessageSource messageSource;

    @Autowired
    private IDemographicService demographicService;

    @GetMapping("/states")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInState>> getStates() {
        return new ResponseEntity<>(demographicService.getStates(), HttpStatus.OK);
    }

    @GetMapping("/states/{stateId}/cities")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInCity>> getCitiesByState(
            @PathVariable(name = "stateId") String stateId,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        log.debug("State Id: {}", stateId);
        return new ResponseEntity<>(demographicService.getCitiesByStateId(stateId, page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/states/{stateId}/cities/{cityId}/colonies")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInColony>> getColonies(
            @PathVariable(name = "stateId") String stateId,
            @PathVariable(name = "cityId") String cityId, @RequestParam(name = "postalCode", required = false) String postalCode,
            @RequestParam(name = "name", required = false) String colonyName,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

        log.debug("StateId: {}, cityId: {}", stateId, cityId);
        return new ResponseEntity<>(demographicService.findColoniesByStateIdAndCityId(stateId, cityId, colonyName, postalCode, page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/colonies")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInColony>> getAllColonies(
            @RequestParam(name = "state", defaultValue = "", required = false) String state,
            @RequestParam(name = "city", defaultValue = "", required = false) String city,
            @RequestParam(name = "colony", defaultValue = "", required = false) String colony,
            @RequestParam(name = "postalCode", defaultValue = "", required = false) String postalCode,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

        log.debug("StateI: {}, city: {}, colony: {}, postalCode: {}", state, city, colony, postalCode);
        return new ResponseEntity<>(demographicService.getAllColonies(state, city, colony, postalCode, page, pageSize), HttpStatus.OK);
    }

}
