package com.audiobea.crm.app.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.commons.dto.DtoInHotdeal;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.commons.mapper.BrandMapper;
import com.audiobea.crm.app.commons.mapper.HotdealMapper;
import com.audiobea.crm.app.commons.mapper.ProductMapper;
import com.audiobea.crm.app.commons.mapper.SubBrandMapper;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.dao.product.IBrandDao;
import com.audiobea.crm.app.dao.product.IHotdealDao;
import com.audiobea.crm.app.dao.product.IProductDao;
import com.audiobea.crm.app.dao.product.ISubBrandDao;
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.dao.product.model.ProductImage;
import com.audiobea.crm.app.dao.product.model.SubBrand;
import com.audiobea.crm.app.utils.Constants;
import com.audiobea.crm.app.utils.Utils;
import com.audiobea.crm.app.utils.Validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service("productService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements IProductService {

    private final MessageSource messageSource;
    @Autowired
    private IUploadService uploadService;
    @Autowired
    private IProductDao productDao;
    @Autowired
    private IBrandDao brandDao;
    @Autowired
    private ISubBrandDao subBrandDao;
    @Autowired
    private IHotdealDao hotdealDao;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SubBrandMapper subBrandMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private HotdealMapper hotdealMapper;

    @Override
    public ResponseData<DtoInProduct> getProducts(String productName, String productType, boolean isNewProduct,
                                                  String brand, String subBrand, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.debug("Marca: {}, SubMarca: {}, ProductType: {}, Nuevo: {}, Page: {}, PageSize: {}",
                brand, subBrand, productType, isNewProduct, page, pageSize);
        productType = StringUtils.isNotBlank(productType) ? productType : "";
        productName = StringUtils.isNotBlank(productName) ? productName : "";
        Page<Product> pageProduct;
        if (isNewProduct) {
            pageProduct = productDao.findByNewProductBrandSubBrandProductType(productName, brand, subBrand, productType, pageable);
        } else {
            pageProduct = productDao.findByBrandSubBrandProductType(productName, brand, subBrand, productType, pageable);
        }
        Validator.validatePage(pageProduct, messageSource);
        return new ResponseData<>(pageProduct.getContent().stream().map(p -> productMapper.productToDtoInProduct(p))
                .collect(Collectors.toList()), pageProduct);
    }

    @Override
    public DtoInProduct getProductById(Long id) {
        return productMapper.productToDtoInProduct(productDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id)))));
    }

    @Transactional
    @Override
    public DtoInProduct saveProduct(DtoInProduct product) {
        log.debug("Product: {}", product);
        return productMapper.productToDtoInProduct(productDao.save(productMapper.productDtoInToProduct(product)));
    }

    @Transactional
    @Override
    public DtoInProduct updateProduct(Long id, DtoInProduct product) {
        Product productFind = productDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id))));
        productFind.setProductName(product.getProductName());
        productFind.setSubBrand(subBrandMapper.subBrandDtoInToSubBrand(product.getSubBrand()));
        productFind.setPrice(product.getPrice());
        productFind.setTitle(product.getTitle());
        productFind.setDescription(product.getDescription());
        return productMapper.productToDtoInProduct(productFind);
    }

    @Transactional
    @Override
    public void deleteProductById(Long id) {
        productDao.deleteById(id);
    }

    @Override
    public ResponseData<DtoInBrand> getBrands(String brandName, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.debug("Marca: {}, Page: {}, PageSize: {}", brandName, page, pageSize);
        Page<Brand> pageBrand;
        if (StringUtils.isNotBlank(brandName)) {
            pageBrand = brandDao.findByBrandNameContains(brandName, pageable);
        } else {
            pageBrand = brandDao.findAll(pageable);
        }
        Validator.validatePage(pageBrand, messageSource);
        return new ResponseData<>(pageBrand.getContent().stream().map(b -> brandMapper.brandToDtoInBrand(b))
                .collect(Collectors.toList()), pageBrand);
    }

    @Override
    public DtoInBrand getBrandById(Long id) {
        return brandMapper.brandToDtoInBrand(brandDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id)))));
    }

    @Transactional
    @Override
    public DtoInBrand saveBrand(DtoInBrand brand) {
        String name = brand.getBrandName().toUpperCase();
        brand.setBrandName(name);
        Brand brandToSave = brandDao.findByBrandName(brand.getBrandName());
        if (brandToSave == null) {
            brand.setBrandName(brand.getBrandName().toUpperCase());
            brandToSave = brandDao.save(brandMapper.brandDtoInToBrand(brand));
        }
        return brandMapper.brandToDtoInBrand(brandToSave);
    }

    @Transactional
    @Override
    public DtoInBrand updateBrand(Long id, DtoInBrand brand) {
        Brand brandToSave = brandDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id))));
        if (brandToSave != null) {
            brandToSave.setBrandName(brand.getBrandName().toUpperCase());
            brandToSave.setEnabled(brand.isEnabled());
            brandDao.save(brandToSave);
        }
        return brandMapper.brandToDtoInBrand(brandToSave);
    }

    @Transactional
    @Override
    public void deleteBrandById(Long id) {
        brandDao.deleteById(id);
    }

    @Override
    public ResponseData<DtoInSubBrand> getSubBrandsByBrandId(String brandId, String subBrand, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Constants.SUB_BRAND_BRAND_NAME).and(Sort.by(Constants.SUB_BRAND)));
        log.debug("MarcaId: {}, SubBrand: {}, Page: {}, PageSize: {}", brandId, subBrand, page, pageSize);
        Page<SubBrand> pageSubBrand = null;
        if (StringUtils.isNotBlank(brandId)) {
            if (brandId.equalsIgnoreCase(Constants.ALL)) {
                log.debug("BrandId: " + brandId);
                pageSubBrand = subBrandDao.findAll(pageable);
            } else if (brandId.chars().allMatch(Character::isDigit)) {
                if (StringUtils.isNotBlank(subBrand)) {
                    log.debug("BrandId Is Not Null && SubBrand Is Not Blank");
                    pageSubBrand = subBrandDao.findByBrandIdAndSubBrandNameContains(Long.valueOf(brandId), subBrand, pageable);
                } else {
                    log.debug("BrandId Is not Null");
                    pageSubBrand = subBrandDao.findByBrandId(Long.valueOf(brandId), pageable);
                }
            }
        }
        Validator.validatePage(pageSubBrand, messageSource);
        return new ResponseData<>(pageSubBrand.getContent().stream().map(sb -> subBrandMapper.subBrandToDtoInSubBrand(sb))
                .collect(Collectors.toList()), pageSubBrand);
    }

    @Override
    public DtoInSubBrand getSubBrandById(Long subBrandId) {
        return subBrandMapper.subBrandToDtoInSubBrand(subBrandDao.findById(subBrandId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(subBrandId)))));
    }

    @Transactional
    @Override
    public DtoInSubBrand saveSubBrand(Long brandId, DtoInSubBrand subBrand) {
        if (StringUtils.isBlank(subBrand.getSubBrandName())) {
            return null;
        }
        Brand brand = brandDao.findById(brandId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(brandId))));
        subBrand.setBrand(brandMapper.brandToDtoInBrand(brand));
        return subBrandMapper.subBrandToDtoInSubBrand(subBrandDao.save(subBrandMapper.subBrandDtoInToSubBrand(subBrand)));
    }

    @Transactional
    @Override
    public DtoInSubBrand updateSubBrand(Long subBrandId, DtoInSubBrand subBrand) {
        SubBrand sbToSave = subBrandDao.findById(subBrandId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(subBrandId))));
        if (sbToSave != null) {
            sbToSave.setSubBrandName(subBrand.getSubBrandName().toUpperCase());
            subBrandDao.save(sbToSave);
        }
        return subBrandMapper.subBrandToDtoInSubBrand(sbToSave);
    }

    @Transactional
    @Override
    public void deleteSubBrandById(Long subBrandId) {
        subBrandDao.deleteById(subBrandId);
    }

    @Transactional
    @Override
    public DtoInProduct uploadImages(Long id, MultipartFile[] files) {
        if (id == null) {
            return null;
        }
        Product product = productMapper.productDtoInToProduct(getProductById(id));
        if (files != null && files.length > 0) {
            log.debug("id: {}, files: {}", id, files.length);
            log.debug("files: {}", files.length);
            List<String> imageNames = uploadService.uploadFiles(files);
            if (imageNames != null && !imageNames.isEmpty()) {
                log.debug("imageNames: {}", imageNames.size());
                List<ProductImage> images = new ArrayList<>();
                for (String imageName : imageNames) {
                    log.debug("Name: {}", imageName);
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
        return hotdealMapper.hotdealToDtoInHotdeal(hotdealDao.findById(1L).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(1L)))));
    }

    @Transactional
    @Override
    public DtoInHotdeal saveHotdeal(DtoInHotdeal hotdeal) {
        hotdeal.setId(1L);
        return hotdealMapper.hotdealToDtoInHotdeal(hotdealDao.save(hotdealMapper.hotdealDtoInToHotdeal(hotdeal)));
    }

}
