package com.audiobea.crm.app.business.dao.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.product.model.SubBrand;

public interface ISubBrandDao extends PagingAndSortingRepository<SubBrand, Long> {

	SubBrand findBySubBrandName(String subMarca);

	Page<SubBrand> findByBrandId(Long brandId, Pageable pageable);

	Page<SubBrand> findByBrandIdAndSubBrandNameContains(Long brandid, String subBrandName, Pageable pageable);

}
