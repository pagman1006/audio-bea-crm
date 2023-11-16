package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IProductDao extends MongoRepository<Product, String> {

	//@Query(value = "{'firstName': {$regex: ?0, $options: 'i'}}")
	//@Query(value = Constants.FIND_PRODUCTS_BY_NEW_PRODUCT_BRAND_SUB_BRAND, nativeQuery = true)
	//Page<Product> findByNewProductBrandSubBrandProductType(String productName, String brandName, String subBrandName, String productType, Pageable pageable);

	//@Query(value = "{'firstName': {$regex: ?0, $options: 'i'}}")
	//@Query(value = Constants.FIND_PRODUCTS_BY_BRAND_SUB_BRAND, nativeQuery = true)
	//Page<Product> findByBrandSubBrandProductType(String productName, String brandName, String subBrandName, String productType, Pageable pageable);

}
