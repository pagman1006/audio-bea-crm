package com.audiobea.crm.app.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.audiobea.crm.app.business.IColonyService;
import com.audiobea.crm.app.dao.customer.IColonyDao;
import com.audiobea.crm.app.dao.customer.model.Colony;

@Service
public class ColonyServiceImpl implements IColonyService {

	@Autowired
	private IColonyDao colonyDao;

	@Override
	public Colony saveColony(Colony colony) {
		return colonyDao.save(colony);
	}

	@Override
	public Page<Colony> findColonies(String state, String city, String codePostal, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name"));
		
//		if (StringUtils.isNotBlank(codePostal)) {
//			return colonyDao.findByPostalCodeContains(codePostal, pageable);
//		}
//		
//		if (StringUtils.isNotBlank(state)) {
//			if (StringUtils.isNotBlank(city)) {
//				return colonyDao.findByStateNameContainsAndCityNameContains(state, city, pageable);
//			}
//			return colonyDao.findByStateNameContains(state, pageable);
//		}
		return colonyDao.findAll(pageable);
	}
}
