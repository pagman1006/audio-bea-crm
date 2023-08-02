package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.utils.Constants;

import org.mapstruct.Mapper;

@Mapper(componentModel = Constants.SPRING)
public interface BrandMapper {

    DtoInBrand brandToDtoInBrand(Brand brands);

    Brand brandDtoInToBrand(DtoInBrand dtoBrands);
}
