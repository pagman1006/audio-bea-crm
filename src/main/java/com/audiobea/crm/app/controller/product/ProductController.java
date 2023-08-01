package com.audiobea.crm.app.controller.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.controller.mapper.ListProductsMapper;
import com.audiobea.crm.app.controller.mapper.ProductMapper;
import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.dao.product.model.ProductImage;
import com.audiobea.crm.app.utils.Validator;

import lombok.AllArgsConstructor;

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
	@Produces({MediaType.APPLICATION_JSON})
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<ResponseData<DtoInProduct>> getProducts(
			@RequestParam(name = "marca", required = false, defaultValue = "") String marca,
			@RequestParam(value = "submarca", required = false, defaultValue = "") String subMarca) {
		Page<Product> pageable = productService.getProducts(marca, subMarca);
		Validator.validatePage(pageable, messageSource);
		ResponseData<DtoInProduct> response = new ResponseData<>(
				listProductsMapper.productsToDtoInProducts(pageable.getContent()), pageable.getNumber(),
				pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@ResponseStatus(value = HttpStatus.OK)
	public Product addProduct(@RequestBody DtoInProduct product) {
		return productService.saveProduct(productMapper.productDtoInToProduct(product));
	}

	@PostMapping(path = "/{id}/image", consumes = { MediaType.MULTIPART_FORM_DATA })
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@ResponseStatus(value = HttpStatus.OK)
	public String uploadImage(@PathVariable("id") Long id,
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
		return uniqueFileName;
	}

	@PostMapping(path = "/{id}/images", consumes = { MediaType.MULTIPART_FORM_DATA })
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@ResponseStatus(value = HttpStatus.OK)
	public List<String> uploadImages(@PathVariable("id") Long id,
			@RequestPart(name = "file", required = false) MultipartFile[] images) {

		return Arrays.asList(images).stream().map(image -> uploadImage(id, image)).collect(Collectors.toList());
	}

	@PutMapping("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	public Product updateProduct(@PathVariable("id") Long id, @RequestBody DtoInProduct product) {
		return productService.updateProduct(id, productMapper.productDtoInToProduct(product));
	}

	@DeleteMapping("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public String deleteProductById(@PathVariable("id") Long id) {
		return productService.deleteProductById(id) ? "Se eliminó correctamente el producto"
				: "Error, ocurrió un error al eliminar el producto";
	}

}
