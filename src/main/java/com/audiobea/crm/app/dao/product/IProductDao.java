package com.audiobea.crm.app.dao.product;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.dao.product.model.SubBrand;

public interface IProductDao extends PagingAndSortingRepository<Product, Long> {

	Product findByProductName(String name);
	List<Product> findBySubBrand(SubBrand subBrand);
	List<Product> findBySubBrandIn(List<SubBrand> brands);
	
}
