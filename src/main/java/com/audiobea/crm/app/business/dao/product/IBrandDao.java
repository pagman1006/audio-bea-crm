package com.audiobea.crm.app.business.dao.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.product.model.Brand;

public interface IBrandDao extends PagingAndSortingRepository<Brand, Long> {
	
	Brand findByBrandName(String brand);
	
	Page<Brand> findByBrandNameContains(String brand, Pageable pageable);

}
