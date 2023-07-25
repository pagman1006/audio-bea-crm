package com.audiobea.crm.app.dao.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.product.model.ProductImage;

public interface IProductImageDao extends PagingAndSortingRepository<ProductImage, Long> {
}
