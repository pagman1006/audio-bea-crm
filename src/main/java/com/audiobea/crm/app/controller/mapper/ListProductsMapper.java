package com.audiobea.crm.app.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.business.dao.product.model.Product;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING, uses = { ProductMapper.class })
public interface ListProductsMapper {

    List<DtoInProduct> productsToDtoInProducts(List<Product> products);

    List<Product> productsDtoInToProducts(List<DtoInProduct> dtoProducts);
}
