package com.audiobea.crm.app.controller.product;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInHotDeal;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.audiobea.crm.app.utils.ConstantsController.PRODUCTS_PATH;
import static com.audiobea.crm.app.utils.ConstantsLog.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(PRODUCTS_PATH)
public class ProductController {

    private final IProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData<DtoInProduct>> getProducts(
            @RequestParam(name = "productType", required = false, defaultValue = "") String productType,
            @RequestParam(name = "productName", required = false, defaultValue = "") String productName,
            @RequestParam(name = "brand", required = false, defaultValue = "") String brand,
            @RequestParam(name = "subBrand", required = false, defaultValue = "") String subBrand,
            @RequestParam(name = "newProduct", required = false) boolean newProduct,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        log.debug(LOG_GET_PRODUCTS, productName, productType, newProduct, brand, subBrand,
                page, pageSize);
        return new ResponseEntity<>(
                productService.getProducts(productName, productType, newProduct, brand, subBrand, page, pageSize),
                HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DtoInProduct> getProductById(@PathVariable("id") String id) {
        log.debug(LOG_GET_PRODUCT_ID, id);
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DtoInProduct> addProduct(@RequestBody @Valid DtoInProduct product) {
        log.debug(LOG_GET_PRODUCT, product);
        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);
    }

    @PostMapping(path = "/{id}/images", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<DtoInProduct> uploadImages(@PathVariable("id") String id,
            @RequestPart(name = "files", required = false) MultipartFile[] images) {
        log.debug(LOG_PRODUCT_ID_IMAGES, id, images.length);
        return new ResponseEntity<>(productService.uploadImages(id, images), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DtoInProduct> updateProduct(@PathVariable("id") String id,
            @RequestBody @Valid DtoInProduct product) {
        log.debug(LOG_UPDATE_PRODUCT);
        return new ResponseEntity<>(productService.updateProduct(id, product), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") String id) {
        log.debug(LOG_DELETE_PRODUCT);
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/hot-deal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DtoInHotDeal> getHotDeal() {
        log.debug(LOG_GET_HOT_DEAL);
        return new ResponseEntity<>(productService.getHotDeal(), HttpStatus.OK);
    }

    @PostMapping(path = "/hot-deal", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DtoInHotDeal> saveHotDeal(@RequestBody DtoInHotDeal hotDeal) {
        log.debug(LOG_SAVE_HOT_DEAL);
        return new ResponseEntity<>(productService.saveHotDeal(hotDeal), HttpStatus.CREATED);
    }

}
