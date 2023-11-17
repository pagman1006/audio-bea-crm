package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.ISubBrandService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.commons.mapper.BrandMapper;
import com.audiobea.crm.app.commons.mapper.SubBrandMapper;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.dao.product.IBrandDao;
import com.audiobea.crm.app.dao.product.ISubBrandDao;
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.dao.product.model.SubBrand;
import com.audiobea.crm.app.utils.Constants;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class SubBrandServiceImpl implements ISubBrandService {

    @Autowired
    private ISubBrandDao subBrandDao;
    @Autowired
    private IBrandDao brandDao;
    @Autowired
    private SubBrandMapper subBrandMapper;
    @Autowired
    private BrandMapper brandMapper;

    private MessageSource messageSource;


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
                    //pageSubBrand = subBrandDao.findByBrandIdAndSubBrandNameContains(Long.valueOf(brandId), subBrand, pageable);
                } else {
                    log.debug("BrandId Is not Null");
                    //pageSubBrand = subBrandDao.findByBrandId(Long.valueOf(brandId), pageable);
                }
            }
        }
        Validator.validatePage(pageSubBrand, messageSource);
        return new ResponseData<>(pageSubBrand.getContent().stream().map(sb -> subBrandMapper.subBrandToDtoInSubBrand(sb))
                .collect(Collectors.toList()), pageSubBrand);
    }

    @Override
    public DtoInSubBrand getSubBrandById(String subBrandId) {
        return subBrandMapper.subBrandToDtoInSubBrand(subBrandDao.findById(subBrandId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), subBrandId))));
    }

    @Override
    public DtoInSubBrand saveSubBrand(String brandId, DtoInSubBrand subBrand) {
        if (StringUtils.isBlank(subBrand.getSubBrandName())) {
            return null;
        }
        Brand brand = brandDao.findById(brandId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), brandId)));
        subBrand.setBrand(brandMapper.brandToDtoInBrand(brand));
        return subBrandMapper.subBrandToDtoInSubBrand(subBrandDao.save(subBrandMapper.subBrandDtoInToSubBrand(subBrand)));
    }

    @Override
    public DtoInSubBrand updateSubBrand(String subBrandId, DtoInSubBrand subBrand) {
        SubBrand sbToSave = subBrandDao.findById(subBrandId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), subBrandId)));
        if (sbToSave != null) {
            sbToSave.setSubBrandName(subBrand.getSubBrandName().toUpperCase());
            subBrandDao.save(sbToSave);
        }
        return subBrandMapper.subBrandToDtoInSubBrand(sbToSave);
    }

    @Override
    public void deleteSubBrandById(String subBrandId) {
        subBrandDao.deleteById(subBrandId);
    }
}