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

    DtoInProduct getProductById(Long id);

    DtoInProduct saveProduct(DtoInProduct product);

    DtoInProduct updateProduct(Long id, DtoInProduct product);

    void deleteProductById(Long id);

    ResponseData<DtoInBrand> getBrands(String brandName, Integer page, Integer pageSize);

    DtoInBrand getBrandById(Long id);

    DtoInBrand saveBrand(DtoInBrand brand);

    DtoInBrand updateBrand(Long id, DtoInBrand brand);

    void deleteBrandById(Long id);

    ResponseData<DtoInSubBrand> getSubBrandsByBrandId(String brandId, String subBrand, Integer page, Integer pageSize);


    DtoInSubBrand getSubBrandById(Long subBrandId);

    DtoInSubBrand saveSubBrand(Long brandId, DtoInSubBrand subBrand);

    DtoInSubBrand updateSubBrand(Long subBrandId, DtoInSubBrand subBrand);

    void deleteSubBrandById(Long subBrandId);

    DtoInProduct uploadImages(Long id, MultipartFile[] files);

    DtoInHotdeal getHotdeal();

    DtoInHotdeal saveHotdeal(DtoInHotdeal hotdeal);

}
