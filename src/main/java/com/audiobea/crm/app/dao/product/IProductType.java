package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.ProductType;
import org.springframework.data.repository.ListCrudRepository;

public interface IProductType extends ListCrudRepository<ProductType, Long> {

}
