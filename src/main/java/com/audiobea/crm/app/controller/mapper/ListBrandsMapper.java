package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.controller.dto.DtoInBrand;
import com.audiobea.crm.app.dao.product.model.Brand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ListBrandsMapper {

    List<DtoInBrand> brandsToDtoInBrands(List<Brand> brands);

    List<Brand> brandsDtoInToBrands(List<DtoInBrand> dtoBrands);
}
