package com.audiobea.crm.app.business.dao.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.product.model.Product;

public interface IProductDao extends PagingAndSortingRepository<Product, Long> {
	
	Page<Product> findBySubBrandBrandBrandNameContainsAndSubBrandSubBrandNameContains(String brandName, String subBrandName, Pageable pageable);
	Page<Product> findBySubBrandBrandBrandNameContains(String brandName, Pageable pageable);
	Page<Product> findBySubBrandSubBrandNameContains(String subBrandName, Pageable pageable);
	
}
