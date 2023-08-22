package com.audiobea.crm.app.business;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.audiobea.crm.app.business.dao.product.model.Brand;
import com.audiobea.crm.app.business.dao.product.model.Product;
import com.audiobea.crm.app.business.dao.product.model.SubBrand;
import com.audiobea.crm.app.commons.dto.EnumProductType;

public interface IProductService {

	public Page<Product> getProducts(EnumProductType productType, boolean newProduct, String marca, String subMarca,
			Integer page, Integer pageSize);

	public Product getProductById(Long id);

	public Product saveProduct(Product product);

	public Product updateProduct(Long id, Product product);

	public boolean deleteProductById(Long id);

	public Page<Brand> getBrands(String brandName, Integer page, Integer pageSize);

	public Brand saveBrand(Brand brand);

	public Brand updateBrand(Long id, Brand brand);

	public boolean deleteBrandById(Long id);

	public Page<SubBrand> getSubBrandsByBrandId(Long brandId, String subBrand, Integer page, Integer pageSize);

	public SubBrand saveSubBrand(Long brandId, SubBrand subBrand);

	public SubBrand updateSubBrand(Long subBrandId, SubBrand subBrand);

	public boolean deleteSubBrandById(Long subBrandId);
	
	public Product uploadImages(Long id, MultipartFile[] files);

}
