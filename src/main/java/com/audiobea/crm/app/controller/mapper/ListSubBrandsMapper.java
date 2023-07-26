package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.dao.product.model.SubBrand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ListSubBrandsMapper {

    List<DtoInSubBrand> subBrandsToDtoInSubBrands(List<SubBrand> subBrands);

    List<SubBrand> subBrandsDtoInToSubBrands(List<DtoInSubBrand> dtoSubBrands);
}
