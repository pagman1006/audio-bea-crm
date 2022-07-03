package com.audiobea.crm.app.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audiobea.crm.app.business.IProductService;
import com.audiobea.crm.app.dao.IBrandDao;
import com.audiobea.crm.app.dao.IProductDao;
import com.audiobea.crm.app.dao.ISubBrandDao;
import com.audiobea.crm.app.dao.model.product.Brand;
import com.audiobea.crm.app.dao.model.product.Product;
import com.audiobea.crm.app.dao.model.product.SubBrand;

@Service("productService")
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

		return products;
	}

	@Override
	public boolean saveProduct(Product product) {

		return false;
	}

	@Override
	public boolean updateProduct(Long id, Product product) {
		Product productFind = productDao.findById(id).orElse(null);
		if (productFind == null) {
			return false;
		}
		productFind.setName(product.getName());
		productFind.setSubBrand(product.getSubBrand());
		productFind.setPrice(product.getPrice());
		productFind.setTitle(product.getTitle());
		productFind.setDescription(product.getDescription());
		return true;
	}

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

	@Override
	public Brand saveBrand(Brand brand) {
		Brand brandToSave = brandDao.findByMarca(brand.getMarca());
		if (brandToSave == null) {
			brandDao.save(brand);
			brandToSave = brand;
		}
		return brandToSave;
	}

	@Override
	public Brand updateBrand(Long id, Brand brand) {
		Brand brandToSave = brandDao.findById(id).orElse(null);
		if (brandToSave != null) {
			brandToSave.setMarca(brand.getMarca());
			brandDao.save(brandToSave);
		}
		return brandToSave;
	}

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

	@Override
	public SubBrand saveSubBrand(Long brandId, SubBrand subBrand) {
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

	@Override
	public SubBrand updateSubBrand(Long subBrandId, SubBrand subBrand) {
		SubBrand sbToSave = subBrandDao.findById(subBrandId).orElse(null);
		if (sbToSave != null) {
			sbToSave.setSubMarca(subBrand.getSubMarca());
			subBrandDao.save(sbToSave);
		}
		return sbToSave;
	}

	@Override
	public boolean deleteSubBrandById(Long subBrandId) {
		subBrandDao.deleteById(subBrandId);
		return true;
	}

}
