package com.audiobea.crm.app.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audiobea.crm.app.business.IImageService;
import com.audiobea.crm.app.dao.product.IImageDao;
import com.audiobea.crm.app.dao.product.model.Image;

@Service
public class ImageServiceImpl implements IImageService {

	@Autowired
	private IImageDao imageDao;
	
	@Override
	public List<Image> getImageCollection() {
		return imageDao.findAll();
	}

}
