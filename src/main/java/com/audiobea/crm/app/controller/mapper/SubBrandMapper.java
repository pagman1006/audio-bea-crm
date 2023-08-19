package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.business.dao.product.model.SubBrand;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.utils.Constants;

import org.mapstruct.Mapper;

@Mapper(componentModel = Constants.SPRING)
public interface SubBrandMapper {

    DtoInSubBrand subBrandToDtoInSubBrand(SubBrand subBrand);

    SubBrand subBrandDtoInToSubBrand(DtoInSubBrand dtoSubBrand);
}
