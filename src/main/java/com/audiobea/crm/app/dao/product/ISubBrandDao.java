package com.audiobea.crm.app.dao.product;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.product.model.SubBrand;

public interface ISubBrandDao extends CrudRepository<SubBrand, Long> {
	
	SubBrand findBySubBrandName(String subMarca);

}
