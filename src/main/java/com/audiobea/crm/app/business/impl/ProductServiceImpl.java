package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInHotdeal;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.commons.mapper.HotdealMapper;
import com.audiobea.crm.app.commons.mapper.ProductMapper;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.dao.product.IHotdealDao;
import com.audiobea.crm.app.dao.product.IProductDao;
import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.dao.product.model.ProductImage;
import com.audiobea.crm.app.utils.Utils;
import com.audiobea.crm.app.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service("productService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IUploadService uploadService;
    @Autowired
    private IProductDao productDao;
    @Autowired
    private IHotdealDao hotdealDao;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private HotdealMapper hotdealMapper;

    private final MessageSource messageSource;

    @Override
    public ResponseData<DtoInProduct> getProducts(String productName, String productType, boolean isNewProduct,
                                                  String brand, String subBrand, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.debug("Marca: {}, SubMarca: {}, ProductType: {}, Nuevo: {}, Page: {}, PageSize: {}",
                brand, subBrand, productType, isNewProduct, page, pageSize);
        productType = StringUtils.isNotBlank(productType) ? productType : "";
        productName = StringUtils.isNotBlank(productName) ? productName : "";
        Page<Product> pageProduct = null;
        if (isNewProduct) {
            //pageProduct = productDao.findByNewProductBrandSubBrandProductType(productName, brand, subBrand, productType, pageable);
        } else {
            //pageProduct = productDao.findByBrandSubBrandProductType(productName, brand, subBrand, productType, pageable);
        }
        Validator.validatePage(pageProduct, messageSource);
        return new ResponseData<>(pageProduct.getContent().stream().map(p -> productMapper.productToDtoInProduct(p))
                .collect(Collectors.toList()), pageProduct);
    }

    @Override
    public DtoInProduct getProductById(String productId) {
        return productMapper.productToDtoInProduct(productDao.findById(productId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), productId))));
    }

    @Transactional
    @Override
    public DtoInProduct saveProduct(DtoInProduct product) {
        log.debug("Product: {}", product);
        return productMapper.productToDtoInProduct(productDao.save(productMapper.productDtoInToProduct(product)));
    }

    @Transactional
    @Override
    public DtoInProduct updateProduct(String productId, DtoInProduct product) {
        Product productFind = productDao.findById(productId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), productId)));
        productFind.setProductName(product.getProductName());
        //productFind.setSubBrandId();
        //productFind.setSubBrand(subBrandMapper.subBrandDtoInToSubBrand(product.getSubBrand()));
        productFind.setPrice(product.getPrice());
        productFind.setTitle(product.getTitle());
        productFind.setDescription(product.getDescription());
        return productMapper.productToDtoInProduct(productFind);
    }

    @Transactional
    @Override
    public void deleteProductById(String productId) {
        productDao.deleteById(productId);
    }

    @Transactional
    @Override
    public DtoInProduct uploadImages(String productId, MultipartFile[] files) {
        if (StringUtils.isBlank(productId)) {
            return null;
        }
        Product product = productMapper.productDtoInToProduct(getProductById(productId));
        if (files != null && files.length > 0) {
            log.debug("id: {}, files: {}", productId, files.length);
            log.debug("files: {}", files.length);
            List<String> imageNames = uploadService.uploadFiles(files);
            if (imageNames != null && !imageNames.isEmpty()) {
                log.debug("imageNames: {}", imageNames.size());
                List<ProductImage> images = new ArrayList<>();
                for (String imageName : imageNames) {
                    log.debug("Name: {}", imageName);
                    new ProductImage(imageName);
                    images.add(new ProductImage(imageName));
                }
                product.setImages(images);
                saveProduct(productMapper.productToDtoInProduct(product));
            }
        }
        return productMapper.productToDtoInProduct(product);
    }

    @Override
    public DtoInHotdeal getHotdeal() {
        return hotdealMapper.hotdealToDtoInHotdeal(hotdealDao.findById("1L").orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(1L)))));
    }

    @Transactional
    @Override
    public DtoInHotdeal saveHotdeal(DtoInHotdeal hotdeal) {
        //hotdeal.setId(1L);
        return hotdealMapper.hotdealToDtoInHotdeal(hotdealDao.save(hotdealMapper.hotdealDtoInToHotdeal(hotdeal)));
    }

}
