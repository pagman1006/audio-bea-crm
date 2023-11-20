package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.HotDeal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IHotdealDao extends MongoRepository<HotDeal, String> {

}
