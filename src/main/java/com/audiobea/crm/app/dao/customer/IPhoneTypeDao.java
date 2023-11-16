package com.audiobea.crm.app.dao.customer;

import com.audiobea.crm.app.dao.customer.model.PhoneType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPhoneTypeDao extends MongoRepository<PhoneType, String> {
}
