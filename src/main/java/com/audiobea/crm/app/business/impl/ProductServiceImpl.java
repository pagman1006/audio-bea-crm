package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.IBrandService;
import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInHotDeal;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.commons.mapper.BrandMapper;
import com.audiobea.crm.app.commons.mapper.HotDealMapper;
import com.audiobea.crm.app.commons.mapper.ProductMapper;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.dao.product.IProductDao;
import com.audiobea.crm.app.dao.product.IProductImageDao;
import com.audiobea.crm.app.dao.product.IProductRankingDao;
import com.audiobea.crm.app.dao.product.IProductTypeDao;
import com.audiobea.crm.app.dao.product.model.IHotDealDao;
import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.dao.product.model.ProductImage;
import com.audiobea.crm.app.dao.product.model.ProductRanking;
import com.audiobea.crm.app.utils.Utils;
import com.audiobea.crm.app.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.audiobea.crm.app.utils.ConstantsLog.*;

@Slf4j
@AllArgsConstructor
@Service("productService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements IProductService {

    private IUploadService uploadService;
    private IBrandService brandService;
    private IProductDao productDao;
    private IProductImageDao productImageDao;
    private IProductRankingDao productRankingDao;
    private IProductTypeDao productTypeDao;
    private IHotDealDao hotDealDao;
    private ProductMapper productMapper;
    private BrandMapper brandMapper;
    private HotDealMapper hotdealMapper;

    private MessageSource messageSource;

    @Override
    public ResponseData<DtoInProduct> getProducts(final String productName, final String productType,
            final boolean isNewProduct,
            final String brandName, final String subBrandName, final Integer page, final Integer pageSize) {
        final Pageable pageable = PageRequest.of(page, pageSize);
        log.debug(LOG_BRAND_SUB_BRAND_TYPE_NEW_PAGE_PAGE_SIZE, brandName,
                subBrandName, productType, isNewProduct, page, pageSize);
        Page<Product> pageProduct;
        if (StringUtils.isNotBlank(productName)) {
            pageProduct = productDao.findByProductName(productName, pageable);
        } else {
            pageProduct = productDao.findAll(pageable);
        }
        Validator.validatePage(pageProduct, messageSource);
        return new ResponseData<>(pageProduct.getContent()
                .stream().map(productMapper::productToDtoInProduct).toList(), pageProduct);
    }

    @Override
    public DtoInProduct getProductById(final String productId) {
        return productMapper.productToDtoInProduct(productDao.findById(productId).orElseThrow(
                () -> new NoSuchElementFoundException(
                        Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), productId))));
    }

    @Transactional
    @Override
    public DtoInProduct saveProduct(final DtoInProduct dtoInProduct) {
        log.debug(LOG_PRODUCT, dtoInProduct);
        final Product product = productMapper.productDtoInToProduct(dtoInProduct);
        return productMapper.productToDtoInProduct(productDao.save(product));
    }

    @Transactional
    @Override
    public DtoInProduct updateProduct(final String productId, final DtoInProduct product) {
        productDao.findById(productId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), productId)));
        product.setId(productId);
        return productMapper.productToDtoInProduct(productDao.save(productMapper.productDtoInToProduct(product)));
    }

    @Transactional
    @Override
    public void deleteProductById(final String productId) {
        productDao.deleteById(productId);
    }

    @Transactional
    @Override
    public DtoInProduct uploadImages(final String productId, final MultipartFile[] files) {
        if (StringUtils.isBlank(productId)) {
            return null;
        }
        final Product product = productMapper.productDtoInToProduct(getProductById(productId));
        if (files != null && files.length > 0) {
            log.debug(LOG_ID_FILES, productId, files.length);
            log.debug(LOG_FILES, files.length);
            final List<String> imageNames = uploadService.uploadFiles(files);
            if (imageNames != null && !imageNames.isEmpty()) {
                log.debug(LOG_IMAGE_NAMES, imageNames.size());
                final List<ProductImage> images = new ArrayList<>();
                for (String imageName : imageNames) {
                    log.debug(LOG_NAME, imageName);
                    images.add(new ProductImage(imageName));
                }
                product.setImages(images);
                saveProduct(productMapper.productToDtoInProduct(product));
            }
        }
        return productMapper.productToDtoInProduct(product);
    }

    @Override
    public DtoInHotDeal getHotDeal() {

        return hotdealMapper.hotdealToDtoInHotdeal(hotDealDao.findAll().stream().findFirst().orElseThrow(
                () -> new NoSuchElementFoundException(
                        Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(),
                                String.valueOf(1L)))));
    }

    @Transactional
    @Override
    public DtoInHotDeal saveHotDeal(final DtoInHotDeal hotDeal) {
        hotDealDao.findAll().stream().findFirst().ifPresent(hDeal -> hotDeal.setId(hDeal.getId()));
        return hotdealMapper.hotdealToDtoInHotdeal(hotDealDao.save(hotdealMapper.hotdealDtoInToHotdeal(hotDeal)));
    }
}
