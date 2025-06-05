package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IProductTypeDao extends MongoRepository<ProductType, String> {

    Optional<ProductType> findProductTypeByType(String type);
}
