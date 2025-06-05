package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInHotdeal;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {

    ResponseData<DtoInProduct> getProducts(String productName, String productType, boolean newProduct, String brandName, String subBrandName,
                                           Integer page, Integer pageSize);

    DtoInProduct getProductById(String productId);

    DtoInProduct saveProduct(DtoInProduct product);

    DtoInProduct updateProduct(String productId, DtoInProduct product);

    void deleteProductById(String productId);

    DtoInProduct uploadImages(String productId, MultipartFile[] files);

    DtoInHotdeal getHotDeal();

    DtoInHotdeal saveHotDeal(DtoInHotdeal hotDeal);

}
