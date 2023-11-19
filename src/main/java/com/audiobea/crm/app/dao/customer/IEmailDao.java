package com.audiobea.crm.app.dao.customer;

import com.audiobea.crm.app.dao.customer.model.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IEmailDao extends MongoRepository<Email, String> {
}
