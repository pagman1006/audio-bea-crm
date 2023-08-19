package com.audiobea.crm.app.controller.mapper;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.business.dao.product.model.Product;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface ProductMapper {

	DtoInProduct productToDtoInProduct(Product product);

	Product productDtoInToProduct(DtoInProduct dtoProduct);
}
