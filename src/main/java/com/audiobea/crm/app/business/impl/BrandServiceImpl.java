package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.IBrandService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.commons.mapper.BrandMapper;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.dao.product.IBrandDao;
import com.audiobea.crm.app.dao.product.model.Brand;
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

import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements IBrandService {

    @Autowired
    private IBrandDao brandDao;
    @Autowired
    private BrandMapper brandMapper;

    private MessageSource messageSource;

    @Override
    public ResponseData<DtoInBrand> getBrands(String brandName, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.debug("Marca: {}, Page: {}, PageSize: {}", brandName, page, pageSize);
        Page<Brand> pageBrand;
        if (StringUtils.isNotBlank(brandName)) {
            pageBrand = brandDao.findByBrandNameContains(brandName.toUpperCase(), pageable);
        } else {
            pageBrand = brandDao.findAll(pageable);
        }
        Validator.validatePage(pageBrand, messageSource);
        return new ResponseData<>(pageBrand.getContent().stream().map(b -> brandMapper.brandToDtoInBrand(b))
                .collect(Collectors.toList()), pageBrand);
    }

    @Override
    public DtoInBrand getBrandById(String brandId) {
        return brandMapper.brandToDtoInBrand(brandDao.findById(brandId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), brandId))));
    }

    @Transactional
    @Override
    public DtoInBrand saveBrand(DtoInBrand brand) {
        brand.setBrandName(brand.getBrandName().toUpperCase());
        Brand brandToSave = brandDao.findByBrandName(brand.getBrandName()).orElse(brandMapper.brandDtoInToBrand(brand));
        return brandMapper.brandToDtoInBrand(brandDao.save(brandToSave));
    }

    @Transactional
    @Override
    public DtoInBrand updateBrand(String brandId, DtoInBrand brand) {
        Brand brandToSave = brandDao.findById(brandId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), brandId)));
        if (brandToSave != null) {
            brandToSave.setBrandName(brand.getBrandName().toUpperCase());
            brandToSave.setEnabled(brand.isEnabled());
            brandDao.save(brandToSave);
        }
        return brandMapper.brandToDtoInBrand(brandToSave);
    }

    @Transactional
    @Override
    public void deleteBrandById(String brandId) {
        brandDao.deleteById(brandId);
    }
}
