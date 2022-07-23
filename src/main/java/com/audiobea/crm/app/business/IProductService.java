package com.audiobea.crm.app.business;

import java.util.List;

import com.audiobea.crm.app.dao.model.product.Brand;
import com.audiobea.crm.app.dao.model.product.Product;
import com.audiobea.crm.app.dao.model.product.SubBrand;

public interface IProductService {
	
	public List<Product> getProducts(String marca, String subMarca);
	public Product getProductById(Long id);
	public Product saveProduct(Product product);
	public Product updateProduct(Long id, Product product); 
	public boolean deleteProductById(Long Id);
	
	public List<Brand> getBrands();
	public Brand saveBrand(Brand brand);
	public Brand updateBrand(Long id, Brand brand);
	public boolean deleteBrandById(Long id);
	
	public List<SubBrand> getSubBrandsByBrandId(Long brandId);
	public SubBrand saveSubBrand(Long brandId, SubBrand subBrand);
	public SubBrand updateSubBrand(Long subBrandId, SubBrand subBrand);
	public boolean deleteSubBrandById(Long subBrandId);
	
}
