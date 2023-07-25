package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.controller.dto.DtoInBrand;
import com.audiobea.crm.app.controller.dto.DtoInSubBrand;
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.dao.product.model.SubBrand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ListSubBrandsMapper {

    List<DtoInSubBrand> subBrandsToDtoInSubBrands(List<SubBrand> subBrands);

    List<SubBrand> subBrandsDtoInToSubBrands(List<DtoInSubBrand> dtoSubBrands);
}
