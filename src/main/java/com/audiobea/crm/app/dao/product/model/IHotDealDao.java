package com.audiobea.crm.app.dao.product.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IHotDealDao extends MongoRepository<HotDeal, String> {
}
