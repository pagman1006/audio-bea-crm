package com.audiobea.crm.app.controller.product;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInBrand;
import com.audiobea.crm.app.commons.dto.DtoInSubBrand;
import com.audiobea.crm.app.controller.mapper.BrandMapper;
import com.audiobea.crm.app.controller.mapper.ListBrandsMapper;
import com.audiobea.crm.app.controller.mapper.ListSubBrandsMapper;
import com.audiobea.crm.app.controller.mapper.SubBrandMapper;
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.dao.product.model.SubBrand;
import com.audiobea.crm.app.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.utils.Utils;
import com.audiobea.crm.app.utils.Validator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/audio-bea/brands")
public class BrandController {

	@Autowired
	private IProductService productService;

	@Autowired
	private ListBrandsMapper listBrandsMapper;

	@Autowired
	private ListSubBrandsMapper listSubBrandsMapper;

	@Autowired
	private BrandMapper brandMapper;

	@Autowired
	private SubBrandMapper subBrandMapper;

	private final MessageSource messageSource;

	// Brand
	@GetMapping
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<ResponseData<DtoInBrand>> getBrands(
			@RequestParam(name = "brand", defaultValue = "", required = false) String brandName,
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		Page<Brand> pageable = productService.getBrands(brandName, page, pageSize);
		Validator.validatePage(pageable, messageSource);
		List<DtoInBrand> listBrands = listBrandsMapper.brandsToDtoInBrands(pageable.getContent());
		ResponseData<DtoInBrand> response = new ResponseData<>(listBrands, pageable.getNumber(), pageable.getSize(),
				pageable.getTotalElements(), pageable.getTotalPages());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public ResponseEntity<DtoInBrand> addBrand(@RequestBody DtoInBrand brand) {
		return new ResponseEntity<>(
				brandMapper.brandToDtoInBrand(productService.saveBrand(brandMapper.brandDtoInToBrand(brand))),
				HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{brand-id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public ResponseEntity<DtoInBrand> updateBrand(@PathVariable("brand-id") Long brandId,
			@RequestBody DtoInBrand brand) {
		return new ResponseEntity<>(brandMapper.brandToDtoInBrand(
				productService.updateBrand(brandId, brandMapper.brandDtoInToBrand(brand))), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{brand-id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<String> deleteBrandById(@PathVariable(value = "brand-id") Long brandId) {
		boolean deleted = productService.deleteBrandById(brandId);
		if (!deleted) {
			throw new NoSuchElementFoundException(
					Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey()));
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// SubBrand
	@GetMapping("/{brand-id}/sub-brands")
	@Produces({ MediaType.APPLICATION_JSON })
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<ResponseData<DtoInSubBrand>> getSubBrandsByBrandId( 
			@RequestParam(name = "subBrand", defaultValue = "", required = false) String subBrand,
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
			@PathVariable(value = "brand-id") Long brandId) {
		Page<SubBrand> pageable = productService.getSubBrandsByBrandId(brandId, subBrand, page, pageSize);
		Validator.validatePage(pageable, messageSource);
		List<DtoInSubBrand> listSubBrands = listSubBrandsMapper.subBrandsToDtoInSubBrands(pageable.getContent());
		ResponseData<DtoInSubBrand> response = new ResponseData<>(listSubBrands, pageable.getNumber(),
				pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/{brand-id}/sub-brands")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<DtoInSubBrand> addSubBrand(@PathVariable(value = "brand-id") Long brandId,
			@RequestBody DtoInSubBrand subBrand) {
		return new ResponseEntity<>(
				subBrandMapper.subBrandToDtoInSubBrand(
						productService.saveSubBrand(brandId, subBrandMapper.subBrandDtoInToSubBrand(subBrand))),
				HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{brand-id}/sub-brands/{sub-brand-id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<DtoInSubBrand> updateSubBrand(@PathVariable(value = "brand-id") Long id,
			@PathVariable(value = "sub-brand-id") Long subBrandId, @RequestBody DtoInSubBrand subBrand) {
		return new ResponseEntity<>(
				subBrandMapper.subBrandToDtoInSubBrand(
						productService.updateSubBrand(subBrandId, subBrandMapper.subBrandDtoInToSubBrand(subBrand))),
				HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{brand-id}/sub-brands/{sub-brand-id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<String> deleteSubBrandById(@PathVariable(value = "sub-brand-id") Long subBrandId) {

		boolean deleted = productService.deleteSubBrandById(subBrandId);
		if (!deleted) {
			throw new NoSuchElementFoundException(
					Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey()));
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
