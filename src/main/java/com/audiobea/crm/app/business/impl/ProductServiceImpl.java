package com.audiobea.crm.app.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.dao.product.IBrandDao;
import com.audiobea.crm.app.dao.product.IProductDao;
import com.audiobea.crm.app.dao.product.ISubBrandDao;
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.dao.product.model.SubBrand;

@Service("productService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements IProductService {

	@Autowired
	private IProductDao productDao;

	@Autowired
	private IBrandDao brandDao;

	@Autowired
	private ISubBrandDao subBrandDao;

	@Override
	public List<Product> getProducts(String marca, String subMarca) {
		List<Product> products = new ArrayList<>();
		if (subMarca != null && !subMarca.isBlank()) {
			SubBrand subBrand = subBrandDao.findBySubBrandName(subMarca);
			if (subBrand != null) {
				products = productDao.findBySubBrand(subBrand);
				return products;
			}
		} else if (marca != null && !marca.isBlank()) {
			Brand brand = brandDao.findByBrandName(marca);
			products = productDao.findBySubBrandIn(brand.getSubBrands());
		} else {
			products = StreamSupport.stream(productDao.findAll().spliterator(), false).collect(Collectors.toList());
		}
		return products;
	}
	
	@Override
	public Product getProductById(Long id) {
		return productDao.findById(id).orElse(null);
	}

	@Transactional(readOnly = false)
	@Override
	public Product saveProduct(Product product) {
		Product productToSave = productDao.findByProductName(product.getProductName());
		if (productToSave == null) {
			productDao.save(product);
		}
		return product;
	}

	@Transactional(readOnly = false)
	@Override
	public Product updateProduct(Long id, Product product) {
		Product productFind = productDao.findById(id).orElse(null);
		if (productFind != null) {
			productFind.setProductName(product.getProductName());
			productFind.setSubBrand(product.getSubBrand());
			productFind.setPrice(product.getPrice());
			productFind.setTitle(product.getTitle());
			productFind.setDescription(product.getDescription());
		}
		return productFind;
	}

	@Transactional(readOnly = false)
	@Override
	public boolean deleteProductById(Long Id) {
		boolean response = false;
		productDao.deleteById(Id);
		response = true;
		return response;
	}

	@Override
	public List<Brand> getBrands() {
		return StreamSupport.stream(brandDao.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Transactional(readOnly = false)
	@Override
	public Brand saveBrand(Brand brand) {
		Brand brandToSave = brandDao.findByBrandName(brand.getBrandName());
		if (brandToSave == null) {
			brand.setBrandName(brand.getBrandName().toUpperCase());
			brandDao.save(brand);
			brandToSave = brand;
		}
		return brandToSave;
	}

	@Transactional(readOnly = false)
	@Override
	public Brand updateBrand(Long id, Brand brand) {
		Brand brandToSave = brandDao.findById(id).orElse(null);
		if (brandToSave != null) {
			brandToSave.setBrandName(brand.getBrandName().toUpperCase());
			brandDao.save(brandToSave);
		}
		return brandToSave;
	}

	@Transactional(readOnly = false)
	@Override
	public boolean deleteBrandById(Long id) {
		brandDao.deleteById(id);
		return true;
	}

	@Override
	public List<SubBrand> getSubBrandsByBrandId(Long brandId) {
		List<SubBrand> listSubBrand = null;
		Brand brand = brandDao.findById(brandId).orElse(null);
		if (brand != null && !brand.getSubBrands().isEmpty()) {
			listSubBrand = new ArrayList<>();
			listSubBrand = brand.getSubBrands();
		}
		return listSubBrand;
	}

	@Transactional(readOnly = false)
	@Override
	public SubBrand saveSubBrand(Long brandId, SubBrand subBrand) {
		subBrand.setSubBrandName(subBrand.getSubBrandName().toUpperCase());
		Brand brand = brandDao.findById(brandId).orElse(null);
		if (brand != null) {
			if (brand.getSubBrands() != null) {
				brand.getSubBrands().add(subBrand);
			} else {
				List<SubBrand> list = new ArrayList<>();
				list.add(subBrand);
				brand.setSubBrands(list);
			}
			brandDao.save(brand);
			for (SubBrand sb : brand.getSubBrands()) {
				if (sb.getSubBrandName().equalsIgnoreCase(subBrand.getSubBrandName())) {
					subBrand = sb;
				}
			}
		} else {
			return null;
		}
		return subBrand;
	}

	@Transactional(readOnly = false)
	@Override
	public SubBrand updateSubBrand(Long subBrandId, SubBrand subBrand) {
		SubBrand sbToSave = subBrandDao.findById(subBrandId).orElse(null);
		if (sbToSave != null) {
			sbToSave.setSubBrandName(subBrand.getSubBrandName().toUpperCase());
			subBrandDao.save(sbToSave);
		}
		return sbToSave;
	}

	@Transactional(readOnly = false)
	@Override
	public boolean deleteSubBrandById(Long subBrandId) {
		subBrandDao.deleteById(subBrandId);
		return true;
	}

}
