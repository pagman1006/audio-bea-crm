package com.audiobea.crm.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.model.product.Brand;

public interface IBrandDao extends CrudRepository<Brand, Long> {
	
	Brand findByBrandName(String marca);

}
