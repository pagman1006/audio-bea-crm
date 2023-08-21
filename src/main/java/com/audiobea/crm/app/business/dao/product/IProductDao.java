package com.audiobea.crm.app.business.dao.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.product.model.Product;

public interface IProductDao extends PagingAndSortingRepository<Product, Long> {
	
	@Query(value = "SELECT * FROM products WHERE sub_brand_id IN (SELECT sub_brand_id FROM sub_brands WHERE sub_brand_name LIKE %:subBrandName% AND brand_id IN (SELECT brand_id FROM brands WHERE brand_name LIKE %:brandName%)) AND new_product = :isNewProduct", nativeQuery = true)
	Page<Product> findByBrandBySubBrandIsNewProduct(String brandName, String subBrandName, Boolean isNewProduct, Pageable pageable);
	
	@Query(value = "SELECT * FROM products WHERE sub_brand_id IN (SELECT sub_brand_id FROM sub_brands WHERE sub_brand_name LIKE %:subBrandName% AND brand_id IN (SELECT brand_id FROM brands WHERE brand_name LIKE %:brandName%))", nativeQuery = true)
	Page<Product> findByBrandBySubBrand(String brandName, String subBrandName, Pageable pageable);
	
	@Query(value = "SELECT * FROM products WHERE new_product = :isNewProduct", nativeQuery = true)
	Page<Product> findByNewProductTrue(boolean isNewProduct, Pageable pageable);
	
}
