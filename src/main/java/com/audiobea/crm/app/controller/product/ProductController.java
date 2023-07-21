package com.audiobea.crm.app.controller.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.dao.product.model.ProductImage;

@RestController
@RequestMapping
public class ProductController {

	@Autowired
	private IProductService productService;

	@Autowired
	private IUploadService uploadService;

	@GetMapping(value = "/productos")
	@ResponseStatus(value = HttpStatus.OK)
	public List<Product> getProducts(@RequestParam(name = "marca", required = false, defaultValue = "") String marca,
			@RequestParam(value = "submarca", required = false, defaultValue = "") String subMarca) {
		System.out.println("Maraca: " + marca + " Submarca: " + subMarca);
		return productService.getProducts(marca, subMarca);
	}

	@PostMapping("/productos")
	@ResponseStatus(value = HttpStatus.OK)
	public Product addProduct(@RequestBody Product product) {
		return productService.saveProduct(product);
	}

	@PostMapping(path = "/productos/{id}/image", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
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

	@PostMapping(path = "/productos/{id}/images", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	public List<String> uploadImages(@PathVariable("id") Long id,
			@RequestPart(name = "file", required = false) MultipartFile[] images) {

		return Arrays.asList(images).stream().map(image -> uploadImage(id, image)).collect(Collectors.toList());
	}

	@PutMapping("/productos/{id}")
	public Product updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
		return productService.updateProduct(id, product);
	}

	@DeleteMapping("/productos/{id}")
	public String deleteProductById(@PathVariable("id") Long id) {
		return productService.deleteProductById(id) ? "Se eliminó correctamente el producto"
				: "Error, ocurrió un error al eliminar el producto";
	}

}
