package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IProductDao extends MongoRepository<Product, String> {

    Page<Product> findByProductName(String productName, Pageable pageable);

}
