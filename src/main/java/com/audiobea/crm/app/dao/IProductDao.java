package com.audiobea.crm.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.model.product.Product;

public interface IProductDao extends CrudRepository<Product, Long> {

	Product findByName(String name);
	
}
