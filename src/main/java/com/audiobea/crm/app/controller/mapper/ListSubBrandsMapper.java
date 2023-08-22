package com.audiobea.crm.app.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.business.dao.product.model.SubBrand;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface ListSubBrandsMapper {

    List<DtoInSubBrand> subBrandsToDtoInSubBrands(List<SubBrand> subBrands);

    List<SubBrand> subBrandsDtoInToSubBrands(List<DtoInSubBrand> dtoSubBrands);
}
