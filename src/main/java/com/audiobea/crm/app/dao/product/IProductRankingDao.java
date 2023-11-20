package com.audiobea.crm.app.dao.product;

import com.audiobea.crm.app.dao.product.model.ProductRanking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IProductRankingDao extends MongoRepository<ProductRanking, String> {
}
