package com.audiobea.crm.app.dao.product;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.dao.product.model.SubBrand;

public interface IProductDao extends CrudRepository<Product, Long> {

	Product findByProductName(String name);
	List<Product> findBySubBrand(SubBrand subBrand);
	List<Product> findBySubBrandIn(List<SubBrand> brands);
	
}
