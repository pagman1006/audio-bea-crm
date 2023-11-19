package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IImageDao extends MongoRepository<Image, String> {

}
