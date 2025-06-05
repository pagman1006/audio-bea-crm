package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInBrand;

public interface IBrandService {

    ResponseData<DtoInBrand> getBrands(String brandName, Integer page, Integer pageSize);

    DtoInBrand getBrandById(String brandId);

    DtoInBrand getBrandByName(String brandName);

    DtoInBrand saveBrand(DtoInBrand brand);

    DtoInBrand updateBrand(String brandId, DtoInBrand brand);

    void deleteBrandById(String brandId);
}
