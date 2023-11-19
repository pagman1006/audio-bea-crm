package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IProductType extends MongoRepository<ProductType, String> {

}
