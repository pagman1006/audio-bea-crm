package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.dao.product.model.SubBrand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubBrandMapper {

    DtoInSubBrand subBrandToDtoInSubBrand(SubBrand subBrand);

    SubBrand subBrandDtoInToSubBrand(DtoInSubBrand dtoSubBrand);
}
