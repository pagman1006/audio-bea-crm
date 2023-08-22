package com.audiobea.crm.app.controller.mapper;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.business.dao.product.model.SubBrand;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface SubBrandMapper {

    DtoInSubBrand subBrandToDtoInSubBrand(SubBrand subBrand);

    SubBrand subBrandDtoInToSubBrand(DtoInSubBrand dtoSubBrand);
}
