package com.audiobea.crm.app.controller.product;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInHotdeal;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInProduct>> getProducts(
            @RequestParam(name = "productType", required = false) String productType,
            @RequestParam(name = "productName", required = false) String productName,
            @RequestParam(name = "brand", required = false, defaultValue = "") String brand,
            @RequestParam(value = "subBrand", required = false, defaultValue = "") String subBrand,
            @RequestParam(name = "newProduct", required = false) boolean newProduct,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return new ResponseEntity<>(productService.getProducts(productName, productType, newProduct, brand, subBrand, page, pageSize), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInProduct> addProduct(@RequestBody DtoInProduct product) {
        log.debug("DtoInProduct: {}", product);
        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);
    }

    @PostMapping(path = "/{id}/images", consumes = {MediaType.MULTIPART_FORM_DATA})
    @PreAuthorize("hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<DtoInProduct> uploadImages(
            @PathVariable("id") Long id,
            @RequestPart(name = "files", required = false) MultipartFile[] images) {
        log.debug("Id: {}, Images: {}", id, images.length);
        return new ResponseEntity<>(productService.uploadImages(id, images), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInProduct> updateProduct(@PathVariable("id") Long id, @RequestBody DtoInProduct product) {
        return new ResponseEntity<>(productService.updateProduct(id, product), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/hotdeal")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInHotdeal> getHotdeal() {
        return new ResponseEntity<>(productService.getHotdeal(), HttpStatus.OK);
    }


    @PostMapping("/hotdeal")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInHotdeal> saveHotdeal(@RequestBody DtoInHotdeal hotdeal) {
        return new ResponseEntity<>(productService.saveHotdeal(hotdeal), HttpStatus.CREATED);
    }

}
