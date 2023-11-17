package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;

public interface ISubBrandService {

    ResponseData<DtoInSubBrand> getSubBrandsByBrandId(String brandId, String subBrand, Integer page, Integer pageSize);
    DtoInSubBrand getSubBrandById(String subBrandId);
    DtoInSubBrand saveSubBrand(String brandId, DtoInSubBrand subBrand);
    DtoInSubBrand updateSubBrand(String subBrandId, DtoInSubBrand subBrand);
    void deleteSubBrandById(String subBrandId);

}
