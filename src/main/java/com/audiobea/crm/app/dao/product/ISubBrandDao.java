package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.SubBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISubBrandDao extends MongoRepository<SubBrand, String> {


    Page<SubBrand> findSubBrandBySubBrandNameContains(String subBrandName, Pageable pageable);
    Page<SubBrand> findSubBrandByBrandIdAndSubBrandNameContains(String brandId, String subBrandName, Pageable pageable);

}
