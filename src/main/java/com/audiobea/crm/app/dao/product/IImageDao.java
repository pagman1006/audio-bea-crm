package com.audiobea.crm.app.dao.product;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.audiobea.crm.app.dao.product.model.Image;

public interface IImageDao extends Repository<Image, Long> {
	
	public List<Image> findAll();
}
