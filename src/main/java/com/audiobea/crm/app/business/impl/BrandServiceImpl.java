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
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.audiobea.crm.app.utils.ConstantsLog.LOG_BRAND_PAGE_PAGE_SIZE;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements IBrandService {

    private IBrandDao brandDao;
    private BrandMapper brandMapper;

    private MessageSource messageSource;

    @Override
    public ResponseData<DtoInBrand> getBrands(final String brandName, final Integer page, final Integer pageSize) {
        final Pageable pageable = PageRequest.of(page, pageSize);
        log.debug(LOG_BRAND_PAGE_PAGE_SIZE, brandName, page, pageSize);
        final Page<Brand> pageBrand;
        if (StringUtils.isNotBlank(brandName)) {
            pageBrand = brandDao.findByBrandNameContains(brandName.toUpperCase(), pageable);
        } else {
            pageBrand = brandDao.findAll(pageable);
        }
        Validator.validatePage(pageBrand, messageSource);
        return new ResponseData<>(pageBrand.getContent().stream().map(brandMapper::brandToDtoInBrand).toList(),
                pageBrand);
    }

    @Override
    public DtoInBrand getBrandById(final String brandId) {
        return brandMapper.brandToDtoInBrand(
                brandDao.findById(brandId).orElseThrow(() -> new NoSuchElementFoundException(
                        Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), brandId))));
    }

    @Override
    public DtoInBrand getBrandByName(final String brandName) {
        return brandMapper.brandToDtoInBrand(brandDao.findByBrandName(brandName)
                .orElseThrow(() -> new NoSuchElementFoundException(
                        Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), brandName))));
    }

    @Transactional
    @Override
    public DtoInBrand saveBrand(final DtoInBrand brand) {
        brand.setBrandName(brand.getBrandName().toUpperCase());
        final Brand brandToSave = brandDao.findByBrandName(brand.getBrandName())
                .orElse(brandMapper.brandDtoInToBrand(brand));
        return brandMapper.brandToDtoInBrand(brandDao.save(brandToSave));
    }

    @Transactional
    @Override
    public DtoInBrand updateBrand(final String brandId, final DtoInBrand brand) {
        final Brand brandToSave = brandDao.findById(brandId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), brandId)));
        brandToSave.setBrandName(brand.getBrandName().toUpperCase());
        brandToSave.setEnabled(brand.isEnabled());
        brandDao.save(brandToSave);
        return brandMapper.brandToDtoInBrand(brandToSave);
    }

    @Transactional
    @Override
    public void deleteBrandById(final String brandId) {
        brandDao.deleteById(brandId);
    }
}
