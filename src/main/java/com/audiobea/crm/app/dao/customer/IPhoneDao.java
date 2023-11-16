package com.audiobea.crm.app.dao.customer;

import com.audiobea.crm.app.dao.customer.model.Phone;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPhoneDao extends MongoRepository<Phone, String> {
}
