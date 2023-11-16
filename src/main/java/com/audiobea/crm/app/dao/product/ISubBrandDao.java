package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.SubBrand;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISubBrandDao extends MongoRepository<SubBrand, String> {

	//SubBrand findBySubBrandName(String subMarca);

	//Page<SubBrand> findByBrandId(Long brandId, Pageable pageable);

	//Page<SubBrand> findByBrandIdAndSubBrandNameContains(Long brandId, String subBrandName, Pageable pageable);

}
