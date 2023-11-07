package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.SubBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ISubBrandDao extends PagingAndSortingRepository<SubBrand, Long>, CrudRepository<SubBrand, Long> {

	SubBrand findBySubBrandName(String subMarca);

	Page<SubBrand> findByBrandId(Long brandId, Pageable pageable);

	Page<SubBrand> findByBrandIdAndSubBrandNameContains(Long brandId, String subBrandName, Pageable pageable);

}
