package com.audiobea.crm.app.dao.invoice;

import com.audiobea.crm.app.dao.invoice.model.CashOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICashOrderDao extends MongoRepository<CashOrder, String> {
}
