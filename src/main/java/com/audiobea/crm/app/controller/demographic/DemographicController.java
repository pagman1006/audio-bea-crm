package com.audiobea.crm.app.controller.demographic;

import com.audiobea.crm.app.business.IDemographicService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCity;
import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.commons.dto.DtoInState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.audiobea.crm.app.utils.ConstantsController.*;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_STATE;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_STATE_CITY;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_STATE_CITY_COLONY_POSTAL_CODE;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(DEMOGRAPHIC_PATH)
public class DemographicController {

	private final IDemographicService demographicService;

    @GetMapping(path = STATES_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData<DtoInState>> getStates() {
		return new ResponseEntity<>(demographicService.getStates(), HttpStatus.OK);
	}

	@GetMapping(path = STATES_ID_CITIES_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData<DtoInCity>> getCitiesByState(
			@PathVariable(name = "stateId") String stateId,
			@RequestParam(name = "name", defaultValue = "", required = false) String cityName,
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		log.debug(LOG_STATE, stateId);
		return new ResponseEntity<>(demographicService.getCitiesByStateId(stateId, cityName, page, pageSize), HttpStatus.OK);
	}

	@GetMapping(path = STATES_ID_CITY_ID_COLONIES_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData<DtoInColony>> getColonies(
			@PathVariable(name = "stateId") String stateId, @PathVariable(name = "cityId") String cityId,
			@RequestParam(name = "name", defaultValue = "", required = false) String colonyName,
			@RequestParam(name = "postalCode", defaultValue = "", required = false) String postalCode,
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

		log.debug(LOG_STATE_CITY, stateId, cityId);
		return new ResponseEntity<>(demographicService.findColoniesByStateIdAndCityId(stateId, cityId, colonyName, postalCode, page, pageSize), HttpStatus.OK);
	}

	@GetMapping(path = COLONIES_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData<DtoInColony>> getAllColonies(
			@RequestParam(name = "state", defaultValue = "", required = false) String state,
			@RequestParam(name = "city", defaultValue = "", required = false) String city,
			@RequestParam(name = "colony", defaultValue = "", required = false) String colony,
			@RequestParam(name = "postalCode", defaultValue = "", required = false) String postalCode,
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

		log.debug(LOG_STATE_CITY_COLONY_POSTAL_CODE, state, city, colony, postalCode);
		return new ResponseEntity<>(demographicService.getAllColonies(state, city, colony, postalCode, page, pageSize), HttpStatus.OK);
	}

}
