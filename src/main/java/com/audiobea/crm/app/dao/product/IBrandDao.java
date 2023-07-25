package com.audiobea.crm.app.dao.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.product.model.Brand;

public interface IBrandDao extends PagingAndSortingRepository<Brand, Long> {
	
	Brand findByBrandName(String marca);

}
