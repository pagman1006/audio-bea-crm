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
import com.audiobea.crm.app.dao.product.*;
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

import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseData<DtoInProduct> getProducts(String productName, String productType, boolean isNewProduct,
            String brandName, String subBrandName, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.debug("Marca: {}, SubMarca: {}, ProductType: {}, Nuevo: {}, Page: {}, PageSize: {}", brandName,
                subBrandName, productType, isNewProduct, page, pageSize);
        Page<Product> pageProduct;
        if (StringUtils.isNotBlank(productName)) {
            pageProduct = productDao.findByProductName(productName, pageable);
        } else {
            pageProduct = productDao.findAll(pageable);
        }
        Validator.validatePage(pageProduct, messageSource);
        return new ResponseData<>(pageProduct.getContent()
                .stream()
                .map(p -> productMapper.productToDtoInProduct(p))
                .collect(Collectors.toList()), pageProduct);
    }

    @Override
    public DtoInProduct getProductById(String productId) {
        return productMapper.productToDtoInProduct(
                productDao.findById(productId)
                        .orElseThrow(() -> new NoSuchElementFoundException(
                                Utils.getLocalMessage(messageSource,
                                        I18Constants.NO_ITEM_FOUND.getKey(),
                                        productId))));
    }

    @Transactional
    @Override
    public DtoInProduct saveProduct(DtoInProduct dtoInProduct) {
        log.debug("Product: {}", dtoInProduct);
        Product product = productMapper.productDtoInToProduct(dtoInProduct);
        return productMapper.productToDtoInProduct(productDao.save(product));
    }

    private List<ProductImage> setProductImages(Product product) {
        Set<String> imageNames = new HashSet<>();
        imageNames.add("product01.png");
        imageNames.add("product02.png");
        imageNames.add("product03.png");
        imageNames.add("product04.png");
        imageNames.add("product05.png");
        List<ProductImage> list = new ArrayList<>();
        int random = new Random().nextInt(5) + 1;
        int count = 0;
        for (String imageName : imageNames) {
            count++;
            ProductImage image = new ProductImage();
            image.setImageName(imageName);
            image.setProductId(product.getId());
            image.setSelected(count == random);
            productImageDao.save(image);
            list.add(image);
        }
        return list;
    }

    private List<ProductRanking> setProductRankings() {
        List<ProductRanking> list = new ArrayList<>();
        int random = new Random().nextInt(10) + 1;
        for (int i = 0; i < random; i++) {
            int rank = new Random().nextInt(5) + 1;
            ProductRanking ranking = new ProductRanking();
            ranking.setRanking(rank);
            productRankingDao.save(ranking);
            list.add(ranking);
        }
        return list;
    }

    @Transactional
    @Override
    public DtoInProduct updateProduct(String productId, DtoInProduct product) {
        productDao.findById(productId)
                .orElseThrow(() -> new NoSuchElementFoundException(
                        Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), productId)));
        product.setId(productId);
        return productMapper.productToDtoInProduct(productDao.save(productMapper.productDtoInToProduct(product)));
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
    public DtoInHotDeal getHotDeal() {

        return hotdealMapper
                .hotdealToDtoInHotdeal(
                        hotDealDao
                                .findAll().stream().findFirst()
                                .orElseThrow(() -> new NoSuchElementFoundException(
                                        Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(),
                                                String.valueOf(1L)))));
    }

    @Transactional
    @Override
    public DtoInHotDeal saveHotDeal(DtoInHotDeal hotDeal) {
        hotDealDao.findAll().stream().findFirst().ifPresent(hDeal -> hotDeal.setId(hDeal.getId()));
        return hotdealMapper.hotdealToDtoInHotdeal(hotDealDao.save(hotdealMapper.hotdealDtoInToHotdeal(hotDeal)));
    }

}
