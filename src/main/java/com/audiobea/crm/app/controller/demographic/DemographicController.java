package com.audiobea.crm.app.controller.demographic;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audiobea.crm.app.business.IColonyService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInColony;
import com.audiobea.crm.app.controller.mapper.ColonyMapper;
import com.audiobea.crm.app.controller.mapper.ListColonyMapper;
import com.audiobea.crm.app.dao.customer.model.Colony;
import com.audiobea.crm.app.exception.NoSuchElementsFoundException;
import com.audiobea.crm.app.utils.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/audio-bea/demographics")
public class DemographicController {

    private final MessageSource messageSource;

    @Autowired
    private IColonyService colonyService;

    @Autowired
    private ListColonyMapper listColonyMapper;
    
    @Autowired
    private ColonyMapper colonyMapper;

    @GetMapping("/colonies")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInColony>> getColonies(@RequestParam(name = "state", required = false) String state, 
    		@RequestParam(name = "city", required = false) String city, 
    		@RequestParam(name = "codePostal", required = false) String codePostal, 
    		@RequestParam(name = "page", defaultValue = "0", required = false) Integer page, 
    		@RequestParam(name = "pageSize",defaultValue = "10", required = false) Integer pageSize) {
    	Page<Colony> pageable = colonyService.findColonies(state, city, codePostal, page, pageSize);
    	
        if (pageable == null || pageable.getContent().isEmpty()) {
            throw new NoSuchElementsFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEMS_FOUND.getKey()));
        }
        List<DtoInColony> listColonies = listColonyMapper.colonyToDtoInColony(pageable.getContent());
        ResponseData<DtoInColony> response = new ResponseData<>(listColonies, pageable.getNumber(),
                pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/colonies")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInColony> saveColonies(@RequestBody Colony colony) {
        return new ResponseEntity<>(colonyMapper.colonyToDtoInColony(colonyService.saveColony(colony)), HttpStatus.CREATED);
    }

}
