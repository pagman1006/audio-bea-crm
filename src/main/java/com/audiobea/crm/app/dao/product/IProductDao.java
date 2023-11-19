package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface IProductDao extends MongoRepository<Product, String> {

    @Query(value = "{'productName': {$regex:  ?0, $options: 'i'}, 'brandId': {$regex:  ?1, $options: 'i'}, 'subBrandId': {$regex:  ?2, $options: 'i'}, 'productTypeId':  {$regex:  ?3, $options: 'i'}, 'productNew':  true}")
    Page<Product> findAllByNameBrandIdSubBrandIdProductTypeIdProductNew(String productName, String brandId,
            String subBrandId, String productTypeId, Boolean productNew, Pageable pageable);

    @Query(value = "{'productName': {$regex:  ?0, $options: 'i'}, 'brandId': {$regex:  ?1, $options: 'i'}, 'subBrandId': {$regex:  ?2, $options: 'i'}, 'productTypeId':  {$regex:  ?3, $options: 'i'}}")
    Page<Product> findAllByNameBrandIdSubBrandIdProductTypeId(String productName, String brandId,
            String subBrandId, String productTypeId, Pageable pageable);

}
