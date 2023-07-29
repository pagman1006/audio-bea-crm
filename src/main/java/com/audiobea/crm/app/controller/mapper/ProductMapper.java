package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.dao.product.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    DtoInProduct productToDtoInProduct(Product product);

    Product productDtoInToProduct(DtoInProduct dtoProduct);
}
