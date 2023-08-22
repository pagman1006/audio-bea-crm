package com.audiobea.crm.app.business.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.business.dao.product.IBrandDao;
import com.audiobea.crm.app.business.dao.product.IProductDao;
import com.audiobea.crm.app.business.dao.product.ISubBrandDao;
import com.audiobea.crm.app.business.dao.product.model.Brand;
import com.audiobea.crm.app.business.dao.product.model.Product;
import com.audiobea.crm.app.business.dao.product.model.SubBrand;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.dto.EnumProductType;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service("productService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements IProductService {

	@Autowired
	private IProductDao productDao;

	@Autowired
	private IBrandDao brandDao;

	@Autowired
	private ISubBrandDao subBrandDao;

	private final MessageSource messageSource;

	@Override
	public Page<Product> getProducts(EnumProductType enumProductType, boolean isNewProduct, String brand, String subBrand,
			Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		log.debug("Marca: {}, SubMarca: {}, ProductType: {}, Nuevo: {}, Page: {}, PageSize: {}", brand, subBrand,
				enumProductType, isNewProduct, page, pageSize);
		String productType = enumProductType != null ? enumProductType.name() : "";
		if (isNewProduct) {
			return productDao.findByNewProductBrandSubBrandProductType(brand, subBrand, productType, pageable);
		} else {
			return productDao.findByBrandSubBrandProductType(brand, subBrand, productType, pageable);
		}
	}

	@Override
	public Product getProductById(Long id) {
		return productDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id))));
	}

	@Transactional
	@Override
	public Product saveProduct(Product product) {
		log.debug("Product: {}", product);
		Product p = productDao.save(product);
		log.debug("Product Saved: {}", p);
		return p;
	}

	@Transactional
	@Override
	public Product updateProduct(Long id, Product product) {
		Product productFind = productDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id))));
		productFind.setProductName(product.getProductName());
		productFind.setSubBrand(product.getSubBrand());
		productFind.setPrice(product.getPrice());
		productFind.setTitle(product.getTitle());
		productFind.setDescription(product.getDescription());
		return productFind;
	}

	@Transactional
	@Override
	public boolean deleteProductById(Long id) {
		boolean response = false;
		productDao.deleteById(id);
		response = true;
		return response;
	}

	@Override
	public Page<Brand> getBrands(String brandName, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		log.debug("Marca: {}, Page: {}, PageSize: {}", brandName, page, pageSize);
		if (StringUtils.isNotBlank(brandName)) {
			return brandDao.findByBrandNameContains(brandName, pageable);
		}
		return brandDao.findAll(pageable);
	}

	@Transactional
	@Override
	public Brand saveBrand(Brand brand) {
		String name = brand.getBrandName().toUpperCase();
		brand.setBrandName(name);
		Brand brandToSave = brandDao.findByBrandName(brand.getBrandName());
		if (brandToSave == null) {
			brand.setBrandName(brand.getBrandName().toUpperCase());
			brandDao.save(brand);
			brandToSave = brand;
		}
		return brandToSave;
	}

	@Transactional
	@Override
	public Brand updateBrand(Long id, Brand brand) {
		Brand brandToSave = brandDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id))));
		if (brandToSave != null) {
			brandToSave.setBrandName(brand.getBrandName().toUpperCase());
			brandToSave.setEnabled(brand.isEnabled());
			brandDao.save(brandToSave);
		}
		return brandToSave;
	}

	@Transactional
	@Override
	public boolean deleteBrandById(Long id) {
		brandDao.deleteById(id);
		return true;
	}

	@Override
	public Page<SubBrand> getSubBrandsByBrandId(Long brandId, String subBrand, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		log.debug("MarcaId: {}, SubBrand: {}, Page: {}, PageSize: {}", brandId, subBrand, page, pageSize);
		if (brandId != null && brandId > 0) {
			if (StringUtils.isNotBlank(subBrand)) {
				log.debug("BrandId Is Not Null && SubBrand Is Not Blank");
				return subBrandDao.findByBrandIdAndSubBrandNameContains(brandId, subBrand, pageable);
			}
			log.debug("BrandId Is not Null");
			return subBrandDao.findByBrandId(brandId, pageable);
		}
		return null;
	}

	@Transactional
	@Override
	public SubBrand saveSubBrand(Long brandId, SubBrand subBrand) {
		if (StringUtils.isBlank(subBrand.getSubBrandName())) {
			return null;
		}
		Brand brand = brandDao.findById(brandId).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(brandId))));
		subBrand.setBrand(brand);
		return subBrandDao.save(subBrand);
	}

	@Transactional
	@Override
	public SubBrand updateSubBrand(Long subBrandId, SubBrand subBrand) {
		SubBrand sbToSave = subBrandDao.findById(subBrandId).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(subBrandId))));
		if (sbToSave != null) {
			sbToSave.setSubBrandName(subBrand.getSubBrandName().toUpperCase());
			subBrandDao.save(sbToSave);
		}
		return sbToSave;
	}

	@Transactional
	@Override
	public boolean deleteSubBrandById(Long subBrandId) {
		subBrandDao.deleteById(subBrandId);
		return true;
	}

}
