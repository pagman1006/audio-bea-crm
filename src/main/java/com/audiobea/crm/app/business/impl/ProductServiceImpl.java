package com.audiobea.crm.app.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.dao.IBrandDao;
import com.audiobea.crm.app.dao.IProductDao;
import com.audiobea.crm.app.dao.ISubBrandDao;
import com.audiobea.crm.app.dao.model.product.Brand;
import com.audiobea.crm.app.dao.model.product.Product;
import com.audiobea.crm.app.dao.model.product.SubBrand;

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
			SubBrand subBrand = subBrandDao.findBySubMarca(subMarca);
			if (subBrand != null) {
				products = productDao.findBySubBrand(subBrand);
				return products;
			}
		} else if (marca != null && !marca.isBlank()) {
			Brand brand = brandDao.findByMarca(marca);
			products = productDao.findBySubBrandIn(brand.getSubMarcas());
		} else {
			products = StreamSupport.stream(productDao.findAll().spliterator(), false).collect(Collectors.toList());
		}
		return products;
	}

	@Transactional(readOnly = false)
	@Override
	public Product saveProduct(Product product) {
		Product productToSave = productDao.findByName(product.getName());
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
			productFind.setName(product.getName());
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
		Brand brandToSave = brandDao.findByMarca(brand.getMarca());
		if (brandToSave == null) {
			brand.setMarca(brand.getMarca().toUpperCase());
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
			brandToSave.setMarca(brand.getMarca().toUpperCase());
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
		if (brand != null && !brand.getSubMarcas().isEmpty()) {
			listSubBrand = new ArrayList<>();
			listSubBrand = brand.getSubMarcas();
		}
		return listSubBrand;
	}

	@Transactional(readOnly = false)
	@Override
	public SubBrand saveSubBrand(Long brandId, SubBrand subBrand) {
		subBrand.setSubMarca(subBrand.getSubMarca().toUpperCase());
		Brand brand = brandDao.findById(brandId).orElse(null);
		if (brand != null) {
			if (brand.getSubMarcas() != null) {
				brand.getSubMarcas().add(subBrand);
			} else {
				List<SubBrand> list = new ArrayList<>();
				list.add(subBrand);
				brand.setSubMarcas(list);
			}
			brandDao.save(brand);
			for (SubBrand sb : brand.getSubMarcas()) {
				if (sb.getSubMarca().equalsIgnoreCase(subBrand.getSubMarca())) {
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
			sbToSave.setSubMarca(subBrand.getSubMarca().toUpperCase());
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
