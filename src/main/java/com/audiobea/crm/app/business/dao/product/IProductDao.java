package com.audiobea.crm.app.business.dao.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.product.model.Product;
import com.audiobea.crm.app.utils.Constants;

public interface IProductDao extends PagingAndSortingRepository<Product, Long> {

	@Query(value = Constants.FIND_PRODUCTS_BY_NEW_PRODUCT_BRAND_SUB_BRAND, nativeQuery = true)
	Page<Product> findByNewProductBrandSubBrandProductType(String productName, String brandName, String subBrandName, String productType, Pageable pageable);

	@Query(value = Constants.FIND_PRODUCTS_BY_BRAND_SUB_BRAND, nativeQuery = true)
	Page<Product> findByBrandSubBrandProductType(String productName, String brandName, String subBrandName, String productType, Pageable pageable);

}
