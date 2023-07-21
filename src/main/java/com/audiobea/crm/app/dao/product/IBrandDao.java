package com.audiobea.crm.app.dao.product;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.product.model.Brand;

public interface IBrandDao extends CrudRepository<Brand, Long> {
	
	Brand findByBrandName(String marca);

}
