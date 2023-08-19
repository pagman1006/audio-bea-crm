package com.audiobea.crm.app.controller.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.business.dao.product.model.Product;
import com.audiobea.crm.app.business.dao.product.model.ProductImage;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.controller.mapper.ListProductsMapper;
import com.audiobea.crm.app.controller.mapper.ProductMapper;
import com.audiobea.crm.app.utils.Validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/v1/audio-bea/products")
public class ProductController {

	@Autowired
	private IProductService productService;

	@Autowired
	private IUploadService uploadService;

	@Autowired
	private ListProductsMapper listProductsMapper;

	@Autowired
	private ProductMapper productMapper;

	private final MessageSource messageSource;

	@GetMapping
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<ResponseData<DtoInProduct>> getProducts(
			@RequestParam(name = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "subBrand", required = false, defaultValue = "") String subBrand,
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		Page<Product> pageable = productService.getProducts(brand, subBrand, page, pageSize);
		Validator.validatePage(pageable, messageSource);
		ResponseData<DtoInProduct> response = new ResponseData<>(
				listProductsMapper.productsToDtoInProducts(pageable.getContent()), pageable.getNumber(),
				pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public ResponseEntity<DtoInProduct> addProduct(@RequestBody DtoInProduct product) {
		log.debug("DtoInProduct: {}", product);
		return new ResponseEntity<>(productMapper.productToDtoInProduct(
				productService.saveProduct(productMapper.productDtoInToProduct(product))), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = "/{id}/image", consumes = { MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public ResponseEntity<String> uploadImage(@PathVariable("id") Long id,
			@RequestPart(name = "file", required = false) MultipartFile image) {
		String uniqueFileName = null;
		if (!image.isEmpty()) {
			try {
				uniqueFileName = uploadService.copy(image);
				Product product = productService.getProductById(id);
				if (product.getImages() == null || product.getImages().isEmpty()) {
					List<ProductImage> listImages = new ArrayList<>();
					ProductImage pImage = new ProductImage();
					pImage.setImageName(uniqueFileName);
					listImages.add(pImage);
					product.setImages(listImages);
				} else {
					ProductImage pImage = new ProductImage();
					pImage.setImageName(uniqueFileName);
					product.getImages().add(pImage);
				}
				productService.saveProduct(product);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(uniqueFileName, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = "/{id}/images", consumes = { MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<List<String>> uploadImages(@PathVariable("id") Long id,
			@RequestPart(name = "file", required = false) MultipartFile[] images) {
		List<String> list = new ArrayList<>();
		for (ResponseEntity<String> r : Arrays.asList(images).stream().map(image -> uploadImage(id, image)).toList()) {
			list.add(r.getBody());
		}
		return new ResponseEntity<>(list, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public ResponseEntity<DtoInProduct> updateProduct(@PathVariable("id") Long id, @RequestBody DtoInProduct product) {
		return new ResponseEntity<>(
				productMapper.productToDtoInProduct(
						productService.updateProduct(id, productMapper.productDtoInToProduct(product))),
				HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<String> deleteProductById(@PathVariable("id") Long id) {
		productService.deleteProductById(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

}
