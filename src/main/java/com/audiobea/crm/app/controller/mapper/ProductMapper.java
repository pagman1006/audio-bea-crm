package com.audiobea.crm.app.controller.mapper;

import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.audiobea.crm.app.business.dao.product.model.Product;
import com.audiobea.crm.app.business.dao.product.model.ProductType;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.commons.dto.EnumProductType;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface ProductMapper {

	@Mapping(target = "productType", source = "productType", qualifiedByName = "enumProductType")
	DtoInProduct productToDtoInProduct(Product product);

	@Mapping(target = "newProduct", ignore = true)
	@Mapping(target = "productType", source = "productType", qualifiedByName = "productType")
	Product productDtoInToProduct(DtoInProduct dtoProduct);
	
	@Named("enumProductType")
	default EnumProductType mapEnumProductType(ProductType productType) {
		if (productType == null || StringUtils.isBlank(productType.getType())) {
			return null;
		}
		return EnumProductType.valueOf(productType.getType());
	}
	
	@Named("productType")
	default ProductType mapProductType(EnumProductType type) {
		if (type == null) {
			return null;
		}
		ProductType productType = new ProductType();
		productType.setType(type.name());
		return productType;
	}
}
