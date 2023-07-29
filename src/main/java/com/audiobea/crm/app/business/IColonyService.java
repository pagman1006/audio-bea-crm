package com.audiobea.crm.app.business;

import org.springframework.data.domain.Page;

import com.audiobea.crm.app.dao.customer.model.Colony;

public interface IColonyService {

	Page<Colony> findColonies(String state, String city, String codePostal, Integer page, Integer pageSize);
    public Colony saveColony(Colony colony);
}
