package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IImageDao extends MongoRepository<Image, String> {
	
	List<Image> findAll();
}
