package com.audiobea.crm.app.business.dao.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.product.model.ProductImage;

public interface IProductImageDao extends PagingAndSortingRepository<ProductImage, Long> {
}
