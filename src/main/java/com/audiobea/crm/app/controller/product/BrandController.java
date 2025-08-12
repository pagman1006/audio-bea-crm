package com.audiobea.crm.app.controller.product;

import com.audiobea.crm.app.business.IBrandService;
import com.audiobea.crm.app.business.ISubBrandService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.utils.Utils;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.audiobea.crm.app.utils.ConstantsController.BRANDS_PATH;
import static com.audiobea.crm.app.utils.ConstantsController.BRAND_ID_SUB_BRANDS_PATH;
import static com.audiobea.crm.app.utils.ConstantsController.SUB_BRAND_ID_SUB_BRAND_ID_PATH;
import static com.audiobea.crm.app.utils.ConstantsLog.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(BRANDS_PATH)
public class BrandController {

    private IBrandService brandService;
    private ISubBrandService subBrandService;

    private MessageSource messageSource;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData<DtoInBrand>> getBrands(
            @RequestParam(name = "brand", defaultValue = "", required = false) String brandName,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        log.debug(LOG_GET_BRANDS);
        return new ResponseEntity<>(brandService.getBrands(brandName, page, pageSize), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DtoInBrand> addBrand(@RequestBody @Valid DtoInBrand brand) {
        log.debug(LOG_ADD_BRAND);
        return new ResponseEntity<>(brandService.saveBrand(brand), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{brand-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DtoInBrand> getBrandById(@PathVariable("brand-id") String brandId) {
        return new ResponseEntity<>(brandService.getBrandById(brandId), HttpStatus.OK);
    }

    @PutMapping(path = "/{brand-id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DtoInBrand> updateBrand(@PathVariable("brand-id") String brandId,
            @RequestBody @Valid DtoInBrand brand) {
        log.debug(LOG_UPDATE_BRAND);
        return new ResponseEntity<>(brandService.updateBrand(brandId, brand), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{brand-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteBrandById(@PathVariable(value = "brand-id") String brandId) {
        log.debug(LOG_DELETE_BRAND);
        brandService.deleteBrandById(brandId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = BRAND_ID_SUB_BRANDS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<ResponseData<DtoInSubBrand>> getSubBrandsByBrandId(
            @RequestParam(name = "subBrand", defaultValue = "", required = false) String subBrand,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @PathVariable(value = "brand-id") String brandId) {
        log.debug(LOG_GET_SUB_BRANDS);
        return new ResponseEntity<>(subBrandService.getSubBrandsByBrandId(brandId, subBrand, page, pageSize),
                HttpStatus.OK);
    }

    @GetMapping(path = SUB_BRAND_ID_SUB_BRAND_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DtoInSubBrand> getSubBrandById(
            @PathVariable(value = "sub-brand-id") String subBrandId, @PathVariable("brand-id") String brandId) {
        if (StringUtils.isNotBlank(brandId)) {
            throw new NoSuchElementFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), brandId));
        }
        return new ResponseEntity<>(subBrandService.getSubBrandById(subBrandId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = BRAND_ID_SUB_BRANDS_PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DtoInSubBrand> addSubBrand(@PathVariable(value = "brand-id") String brandId,
            @RequestBody @Valid DtoInSubBrand subBrand) {
        log.debug(LOG_ADD_SUB_BRAND);
        return new ResponseEntity<>(subBrandService.saveSubBrand(brandId, subBrand), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = SUB_BRAND_ID_SUB_BRAND_ID_PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DtoInSubBrand> updateSubBrand(
            @PathVariable(value = "brand-id") String id,
            @PathVariable(value = "sub-brand-id") String subBrandId, @RequestBody @Valid DtoInSubBrand subBrand) {
        log.debug(LOG_UPDATE_SUB_BRAND);
        return new ResponseEntity<>(subBrandService.updateSubBrand(subBrandId, subBrand), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = SUB_BRAND_ID_SUB_BRAND_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> deleteSubBrandById(
            @PathVariable(value = "sub-brand-id") String subBrandId, @PathVariable("brand-id") String brandId) {
        log.debug(LOG_DELETE_SUB_BRAND);
        if (StringUtils.isNotBlank(brandId)) {
            throw new NoSuchElementFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), brandId));
        }
        subBrandService.deleteSubBrandById(subBrandId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
