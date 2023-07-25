package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.controller.dto.DtoInBrand;
import com.audiobea.crm.app.controller.dto.DtoInSubBrand;
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.dao.product.model.SubBrand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubBrandMapper {

    DtoInSubBrand subBrandToDtoInSubBrand(SubBrand subBrand);

    SubBrand subBrandDtoInToSubBrand(DtoInSubBrand dtoSubBrand);
}
