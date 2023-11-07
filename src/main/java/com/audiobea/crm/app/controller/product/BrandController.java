package com.audiobea.crm.app.controller.product;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.utils.Constants;
import com.audiobea.crm.app.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/brands")
public class BrandController {

    private final MessageSource messageSource;
    @Autowired
    private IProductService productService;

    @GetMapping
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInBrand>> getBrands(
            @RequestParam(name = "brand", defaultValue = "", required = false) String brandName,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        log.debug("getBrands");
        return new ResponseEntity<>(productService.getBrands(brandName, page, pageSize), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInBrand> addBrand(@RequestBody DtoInBrand brand) {
        log.debug("addBrand");
        return new ResponseEntity<>(productService.saveBrand(brand), HttpStatus.CREATED);
    }

    @PutMapping("/{brand-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInBrand> updateBrand(@PathVariable("brand-id") Long brandId, @RequestBody DtoInBrand brand) {
        log.debug("updateBrand");
        return new ResponseEntity<>(productService.updateBrand(brandId, brand), HttpStatus.CREATED);
    }

    @GetMapping("/{brand-id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInBrand> getBrandById(@PathVariable("brand-id") Long brandId) {
        return new ResponseEntity<>(productService.getBrandById(brandId), HttpStatus.OK);
    }

    @DeleteMapping("/{brand-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<String> deleteBrandById(@PathVariable(value = "brand-id") Long brandId) {
        log.debug("deleteBrand");
        if (!productService.deleteBrandById(brandId)) {
            throw new NoSuchElementFoundException(Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey()));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{brand-id}/sub-brands")
    @Produces({MediaType.APPLICATION_JSON})
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<ResponseData<DtoInSubBrand>> getSubBrandsByBrandId(
            @RequestParam(name = "subBrand", defaultValue = "", required = false) String subBrand,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @PathVariable(value = "brand-id") String brandId) {
        log.debug("getSubBrandsByBrandId");
        return new ResponseEntity<>(productService.getSubBrandsByBrandId(brandId, subBrand, page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{brand-id}/sub-brands/{sub-brand-id}")
    public ResponseEntity<DtoInSubBrand> getSubBrandById(@PathVariable(value = "sub-brand-id") Long subBrandId) {
        return new ResponseEntity<>(productService.getSubBrandById(subBrandId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{brand-id}/sub-brands")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DtoInSubBrand> addSubBrand(@PathVariable(value = "brand-id") Long brandId, @RequestBody DtoInSubBrand subBrand) {
        log.debug("addSubBrand");
        return new ResponseEntity<>(productService.saveSubBrand(brandId, subBrand), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{brand-id}/sub-brands/{sub-brand-id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DtoInSubBrand> updateSubBrand(
            @PathVariable(value = "brand-id") Long id,
            @PathVariable(value = "sub-brand-id") Long subBrandId, @RequestBody DtoInSubBrand subBrand) {
        log.debug("updateSubBrand");
        return new ResponseEntity<>(productService.updateSubBrand(subBrandId, subBrand), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{brand-id}/sub-brands/{sub-brand-id}")
    @Produces({MediaType.APPLICATION_JSON})
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> deleteSubBrandById(@PathVariable(value = "sub-brand-id") Long subBrandId) {
        log.debug("deleteSubBrandById");
        if (!productService.deleteSubBrandById(subBrandId)) {
            throw new NoSuchElementFoundException(Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey()));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
