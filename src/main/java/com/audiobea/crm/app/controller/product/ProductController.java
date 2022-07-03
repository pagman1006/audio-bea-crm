package com.audiobea.crm.app.controller.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.dao.model.product.Product;

@RestController
@RequestMapping
public class ProductController {

	@Autowired
	private IProductService productService;

	@GetMapping(value = { "/productos", "/marcas/{marca}/submarcas/{submarca}/productos" })
	@ResponseStatus(value = HttpStatus.OK)
	public List<Product> getProducts(@PathVariable(value = "marca", required = false) String marca,
			@PathVariable(value = "submarca", required = false) String submarca) {
		return productService.getProducts(marca, submarca);
	}

	@PostMapping("/productos")
	@ResponseStatus(value = HttpStatus.OK)
	public String addProduct(@RequestBody Product product) {
		
		System.out.println(product.toString());
		
		String result = productService.saveProduct(product) ? "Producto insertado"
				: "Error, ocurrió un error al guardar el producto";
		return result;
	}

	@PutMapping("/productos/{id}")
	public String updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
		return productService.updateProduct(id, product) ? "Se actualizó correctamente el producto"
				: "Error, ocurrió un error al actualizar el producto";
	}

	@DeleteMapping("/productos/{id}")
	public String deleteProductById(@PathVariable("id") Long id) {
		return productService.deleteProductById(id) ? "Se eliminó correctamente el producto"
				: "Error, ocurrió un error al eliminar el producto";
	}

}
