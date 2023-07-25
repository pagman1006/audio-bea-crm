package com.audiobea.crm.app.dao.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.product.model.SubBrand;

public interface ISubBrandDao extends PagingAndSortingRepository<SubBrand, Long> {
	
	SubBrand findBySubBrandName(String subMarca);

}
