package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface IProductDao extends PagingAndSortingRepository<Product, Long>, CrudRepository<Product, Long> {

    @Query(value = Constants.FIND_PRODUCTS_BY_NEW_PRODUCT_BRAND_SUB_BRAND, nativeQuery = true)
    Page<Product> findByNewProductBrandSubBrandProductType(@Param("productName") String productName, @Param("brandName") String brandName, @Param("subBrandName") String subBrandName, @Param("productType") String productType, Pageable pageable);

    @Query(value = Constants.FIND_PRODUCTS_BY_BRAND_SUB_BRAND, nativeQuery = true)
    Page<Product> findByBrandSubBrandProductType(@Param("productName") String productName, @Param("brandName") String brandName, @Param("subBrandName") String subBrandName, @Param("productType") String productType, Pageable pageable);

}
