package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.ProductImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IProductImageDao extends MongoRepository<ProductImage, String> {
}
