package com.audiobea.crm.app.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface ListBrandsMapper {

    List<DtoInBrand> brandsToDtoInBrands(List<Brand> brands);

    List<Brand> brandsDtoInToBrands(List<DtoInBrand> dtoBrands);
}
