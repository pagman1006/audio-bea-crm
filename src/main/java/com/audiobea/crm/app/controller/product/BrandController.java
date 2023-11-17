package com.audiobea.crm.app.controller.product;

import com.audiobea.crm.app.business.IBrandService;
import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.business.ISubBrandService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/brands")
public class BrandController {

    @Autowired
    private IProductService productService;
    @Autowired
    private IBrandService brandService;
    @Autowired
    private ISubBrandService subBrandService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData<DtoInBrand>> getBrands(
            @RequestParam(name = "brand", defaultValue = "", required = false) String brandName,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        log.debug("getBrands");
        return new ResponseEntity<>(brandService.getBrands(brandName, page, pageSize), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DtoInBrand> addBrand(@RequestBody DtoInBrand brand) {
        log.debug("addBrand");
        return new ResponseEntity<>(brandService.saveBrand(brand), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{brand-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DtoInBrand> getBrandById(@PathVariable("brand-id") String brandId) {
        return new ResponseEntity<>(brandService.getBrandById(brandId), HttpStatus.OK);
    }

    @PutMapping(path = "/{brand-id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DtoInBrand> updateBrand(@PathVariable("brand-id") String brandId, @RequestBody DtoInBrand brand) {
        log.debug("updateBrand");
        return new ResponseEntity<>(brandService.updateBrand(brandId, brand), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{brand-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteBrandById(@PathVariable(value = "brand-id") String brandId) {
        log.debug("deleteBrand");
        brandService.deleteBrandById(brandId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/{brand-id}/sub-brands", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<ResponseData<DtoInSubBrand>> getSubBrandsByBrandId(
            @RequestParam(name = "subBrand", defaultValue = "", required = false) String subBrand,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @PathVariable(value = "brand-id") String brandId) {
        log.debug("getSubBrandsByBrandId");
        return new ResponseEntity<>(subBrandService.getSubBrandsByBrandId(brandId, subBrand, page, pageSize), HttpStatus.OK);
    }

    @GetMapping(path = "/{brand-id}/sub-brands/{sub-brand-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DtoInSubBrand> getSubBrandById(@PathVariable(value = "sub-brand-id") String subBrandId) {
        return new ResponseEntity<>(subBrandService.getSubBrandById(subBrandId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "/{brand-id}/sub-brands", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DtoInSubBrand> addSubBrand(@PathVariable(value = "brand-id") String brandId, @RequestBody DtoInSubBrand subBrand) {
        log.debug("addSubBrand");
        return new ResponseEntity<>(subBrandService.saveSubBrand(brandId, subBrand), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/{brand-id}/sub-brands/{sub-brand-id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DtoInSubBrand> updateSubBrand(
            @PathVariable(value = "brand-id") String id,
            @PathVariable(value = "sub-brand-id") String subBrandId, @RequestBody DtoInSubBrand subBrand) {
        log.debug("updateSubBrand");
        return new ResponseEntity<>(subBrandService.updateSubBrand(subBrandId, subBrand), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/{brand-id}/sub-brands/{sub-brand-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> deleteSubBrandById(@PathVariable(value = "sub-brand-id") String subBrandId) {
        log.debug("deleteSubBrandById");
        subBrandService.deleteSubBrandById(subBrandId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
