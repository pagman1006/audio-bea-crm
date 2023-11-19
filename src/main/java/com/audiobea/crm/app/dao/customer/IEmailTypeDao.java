package com.audiobea.crm.app.dao.customer;

import com.audiobea.crm.app.dao.customer.model.EmailType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IEmailTypeDao extends MongoRepository<EmailType, String> {
}
