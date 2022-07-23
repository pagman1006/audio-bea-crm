package com.audiobea.crm.app.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.model.product.Product;
import com.audiobea.crm.app.dao.model.product.SubBrand;

public interface IProductDao extends CrudRepository<Product, Long> {

	Product findByProductName(String name);
	List<Product> findBySubBrand(SubBrand subBrand);
	List<Product> findBySubBrandIn(List<SubBrand> brands);
	
}
