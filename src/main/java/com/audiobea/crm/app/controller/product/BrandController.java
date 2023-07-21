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
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.dao.product.model.SubBrand;

@RestController
@RequestMapping("/marcas")
public class BrandController {

	@Autowired
	private IProductService productService;

	// Brand
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<Brand> getBrands() {
		return productService.getBrands();
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Brand addBrand(@RequestBody Brand brand) {
		return productService.saveBrand(brand);
	}

	@PutMapping("/{brand-id}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Brand updateBrand(@PathVariable("brand-id") Long brandId, @RequestBody Brand brand) {
		return productService.updateBrand(brandId, brand);
	}

	@DeleteMapping("/{brand-id}")
	@ResponseStatus(code = HttpStatus.OK)
	public String deleteBrandById(@PathVariable(value = "brand-id") Long brandId) {
		String respuesta = productService.deleteBrandById(brandId) ? "Se eliminó correctamente"
				: "Error, ocurrió un error al eliminar el registro";
		return respuesta;
	}

	// SubBrand
	@GetMapping("/{brand-id}/sub-marcas")
	@ResponseStatus(code = HttpStatus.OK)
	public List<SubBrand> getSubBrandsByBrandId(@PathVariable(value = "brand-id") Long brandId) {
		return productService.getSubBrandsByBrandId(brandId);
	}

	@PostMapping("/{brand-id}/sub-marcas")
	@ResponseStatus(code = HttpStatus.CREATED)
	public SubBrand addSubBrand(@PathVariable(value = "brand-id") Long brandId, @RequestBody SubBrand subBrand) {
		return productService.saveSubBrand(brandId, subBrand);
	}

	@PutMapping("/{brand-id}/sub-marcas/{sub-brand-id}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public SubBrand updateSubBrand(@PathVariable(value = "brand-id") Long id,
			@PathVariable(value = "sub-brand-id") Long subBrandId, @RequestBody SubBrand subBrand) {
		return productService.updateSubBrand(id, subBrand);
	}

	@DeleteMapping("/{brand-id}/sub-marcas/{sub-brand-id}")
	@ResponseStatus(code = HttpStatus.OK)
	public String deleteSubBrandById(@PathVariable(value = "sub-brand-id") Long subBrandId) {
		String respuesta = productService.deleteSubBrandById(subBrandId) ? "Se eliminó correctamente"
				: "Error, ocurrió un error al eliminar el registro";
		return respuesta;
	}

}
