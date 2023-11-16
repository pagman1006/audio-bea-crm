package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IBrandDao extends MongoRepository<Brand, String> {

	Brand findByBrandName(String brand);

	Page<Brand> findByBrandNameContains(String brand, Pageable pageable);

}
