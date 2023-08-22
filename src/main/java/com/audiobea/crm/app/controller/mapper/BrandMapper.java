package com.audiobea.crm.app.controller.mapper;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.business.dao.product.model.Brand;
import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface BrandMapper {

    DtoInBrand brandToDtoInBrand(Brand brands);

    Brand brandDtoInToBrand(DtoInBrand dtoBrands);
}
