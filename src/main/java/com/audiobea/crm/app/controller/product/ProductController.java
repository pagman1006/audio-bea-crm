package com.audiobea.crm.app.controller.product;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInHotdeal;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/products")
public class ProductController {

	private final IProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData<DtoInProduct>> getProducts(
			@RequestParam(name = "productType", required = false) String productType,
			@RequestParam(name = "productName", required = false) String productName,
			@RequestParam(name = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "subBrand", required = false, defaultValue = "") String subBrand,
			@RequestParam(name = "newProduct", required = false) boolean newProduct,
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		log.debug("getProducts");
		log.debug("PageSize: {}", pageSize);
		return new ResponseEntity<>(
				productService.getProducts(productName, productType, newProduct, brand, subBrand, page, pageSize),
				HttpStatus.OK);
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DtoInProduct> getProductById(@PathVariable("id") String id) {
		log.debug("getProductById: {}", id);
		return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<DtoInProduct> addProduct(@RequestBody DtoInProduct product) {
		log.debug("DtoInProduct: {}", product);
		return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);
	}

	@PostMapping(path = "/{id}/images", consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<DtoInProduct> uploadImages(@PathVariable("id") String id,
			@RequestPart(name = "files", required = false) MultipartFile[] images) {
		log.debug("Id: {}, Images: {}", id, images.length);
		return new ResponseEntity<>(productService.uploadImages(id, images), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<DtoInProduct> updateProduct(@PathVariable("id") String id, @RequestBody DtoInProduct product) {
		log.debug("updateProduct");
		return new ResponseEntity<>(productService.updateProduct(id, product), HttpStatus.CREATED);
	}

	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteProductById(@PathVariable("id") String id) {
		log.debug("deleteProductById");
		productService.deleteProductById(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@GetMapping(path = "/hot-deal", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DtoInHotdeal> getHotDeal() {
		log.debug("getHotDeal");
		return new ResponseEntity<>(productService.getHotDeal(), HttpStatus.OK);
	}

	@PostMapping(path = "/hot-deal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DtoInHotdeal> saveHotDeal(@RequestBody DtoInHotdeal hotDeal) {
		log.debug("saveHotDeal");
		return new ResponseEntity<>(productService.saveHotDeal(hotDeal), HttpStatus.CREATED);
	}

}
