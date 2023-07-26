package com.audiobea.crm.app.controller.product;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.controller.mapper.BrandMapper;
import com.audiobea.crm.app.controller.mapper.ListBrandsMapper;
import com.audiobea.crm.app.controller.mapper.ListSubBrandsMapper;
import com.audiobea.crm.app.controller.mapper.SubBrandMapper;
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.dao.product.model.SubBrand;
import com.audiobea.crm.app.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.exception.NoSuchElementsFoundException;
import com.audiobea.crm.app.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ListBrandsMapper listBrandsMapper;

    @Autowired
    private ListSubBrandsMapper listSubBrandsMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SubBrandMapper subBrandMapper;

    private final MessageSource messageSource;

    // Brand
    @GetMapping
    public ResponseEntity<ResponseData<DtoInBrand>> getBrands(@RequestParam(name = "brand", defaultValue = "", required = false) String brandName) {
        Page<Brand> pageable = productService.getBrands(brandName);
        if (pageable == null || pageable.getContent().isEmpty()) {
            throw new NoSuchElementsFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEMS_FOUND.getKey()));
        }
        List<DtoInBrand> listBrands = listBrandsMapper.brandsToDtoInBrands(pageable.getContent());
        ResponseData<DtoInBrand> response = new ResponseData<>(listBrands, pageable.getNumber(),
                pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DtoInBrand> addBrand(@RequestBody Brand brand) {
        return new ResponseEntity<>(brandMapper.brandToDtoInBrand(productService.saveBrand(brand)), HttpStatus.CREATED);
    }

    @PutMapping("/{brand-id}")
    public ResponseEntity<DtoInBrand> updateBrand(@PathVariable("brand-id") Long brandId, @RequestBody Brand brand) {
        return new ResponseEntity<>(brandMapper.brandToDtoInBrand(productService.updateBrand(brandId, brand)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{brand-id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> deleteBrandById(@PathVariable(value = "brand-id") Long brandId) {
        boolean deleted = productService.deleteBrandById(brandId);
        if (!deleted) {
            throw new NoSuchElementFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey()));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // SubBrand
    @GetMapping("/{brand-id}/sub-brands")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<ResponseData<DtoInSubBrand>> getSubBrandsByBrandId(@PathVariable(value = "brand-id") Long brandId) {
        Page<SubBrand> pageable = productService.getSubBrandsByBrandId(brandId);
        if (pageable == null || pageable.getContent().isEmpty()) {
            throw new NoSuchElementsFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEMS_FOUND.getKey()));
        }
        List<DtoInSubBrand> listSubBrands = listSubBrandsMapper.subBrandsToDtoInSubBrands(pageable.getContent());
        ResponseData<DtoInSubBrand> response = new ResponseData<>(listSubBrands, pageable.getNumber(),
                pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{brand-id}/sub-brands")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DtoInSubBrand> addSubBrand(@PathVariable(value = "brand-id") Long brandId, @RequestBody SubBrand subBrand) {
        return new ResponseEntity<>(subBrandMapper.subBrandToDtoInSubBrand(productService.saveSubBrand(brandId, subBrand)), HttpStatus.CREATED);
    }

    @PutMapping("/{brand-id}/sub-brands/{sub-brand-id}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DtoInSubBrand> updateSubBrand(@PathVariable(value = "brand-id") Long id,
                                   @PathVariable(value = "sub-brand-id") Long subBrandId, @RequestBody SubBrand subBrand) {
        return new ResponseEntity<>(subBrandMapper.subBrandToDtoInSubBrand(productService.updateSubBrand(subBrandId, subBrand)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{brand-id}/sub-brands/{sub-brand-id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> deleteSubBrandById(@PathVariable(value = "sub-brand-id") Long subBrandId) {

        boolean deleted = productService.deleteSubBrandById(subBrandId);
        if (!deleted) {
            throw new NoSuchElementFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey()));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
