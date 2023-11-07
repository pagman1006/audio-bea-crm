package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.Image;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface IImageDao extends Repository<Image, Long> {
	
	List<Image> findAll();
}
