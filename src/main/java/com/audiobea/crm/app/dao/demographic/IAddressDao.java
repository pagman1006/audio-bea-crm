package com.audiobea.crm.app.dao.demographic;

import com.audiobea.crm.app.dao.demographic.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IAddressDao extends MongoRepository<Address, String> {
}
