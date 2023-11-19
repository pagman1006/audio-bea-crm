package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;

@Mapper(componentModel = Constants.SPRING)
public interface ProductMapper {

	DtoInProduct productToDtoInProduct(Product product);

	Product productDtoInToProduct(DtoInProduct dtoProduct);

}
