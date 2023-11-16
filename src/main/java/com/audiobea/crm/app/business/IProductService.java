package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.commons.dto.DtoInHotdeal;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {

    ResponseData<DtoInProduct> getProducts(String productName, String productType, boolean newProduct, String marca, String subMarca,
                                           Integer page, Integer pageSize);

    DtoInProduct getProductById(String id);

    DtoInProduct saveProduct(DtoInProduct product);

    DtoInProduct updateProduct(String id, DtoInProduct product);

    void deleteProductById(String id);

    ResponseData<DtoInBrand> getBrands(String brandName, Integer page, Integer pageSize);

    DtoInBrand getBrandById(String id);

    DtoInBrand saveBrand(DtoInBrand brand);

    DtoInBrand updateBrand(String id, DtoInBrand brand);

    void deleteBrandById(String id);

    ResponseData<DtoInSubBrand> getSubBrandsByBrandId(String brandId, String subBrand, Integer page, Integer pageSize);


    DtoInSubBrand getSubBrandById(String subBrandId);

    DtoInSubBrand saveSubBrand(String brandId, DtoInSubBrand subBrand);

    DtoInSubBrand updateSubBrand(String subBrandId, DtoInSubBrand subBrand);

    void deleteSubBrandById(String subBrandId);

    DtoInProduct uploadImages(String id, MultipartFile[] files);

    DtoInHotdeal getHotdeal();

    DtoInHotdeal saveHotdeal(DtoInHotdeal hotdeal);

}
