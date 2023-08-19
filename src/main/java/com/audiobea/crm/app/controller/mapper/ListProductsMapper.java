package com.audiobea.crm.app.controller.mapper;

import com.audiobea.crm.app.business.dao.product.model.Product;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.utils.Constants;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = Constants.SPRING)
public interface ListProductsMapper {

    List<DtoInProduct> productsToDtoInProducts(List<Product> products);

    List<Product> productsDtoInToProducts(List<DtoInProduct> dtoProducts);
}
