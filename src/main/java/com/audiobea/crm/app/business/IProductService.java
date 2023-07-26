package com.audiobea.crm.app.business;

import java.util.List;

import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.dao.product.model.SubBrand;
import org.springframework.data.domain.Page;

public interface IProductService {
	
	public Page<Product> getProducts(String marca, String subMarca);
	public Product getProductById(Long id);
	public Product saveProduct(Product product);
	public Product updateProduct(Long id, Product product); 
	public boolean deleteProductById(Long id);
	
	public Page<Brand> getBrands(String brandName);
	public Brand saveBrand(Brand brand);
	public Brand updateBrand(Long id, Brand brand);
	public boolean deleteBrandById(Long id);
	
	public Page<SubBrand> getSubBrandsByBrandId(Long brandId);
	public SubBrand saveSubBrand(Long brandId, SubBrand subBrand);
	public SubBrand updateSubBrand(Long subBrandId, SubBrand subBrand);
	public boolean deleteSubBrandById(Long subBrandId);
	
}
