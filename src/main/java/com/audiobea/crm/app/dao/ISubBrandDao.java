package com.audiobea.crm.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.model.product.SubBrand;

public interface ISubBrandDao extends CrudRepository<SubBrand, Long> {
	
	SubBrand findBySubBrandName(String subMarca);

}
