package com.audiobea.crm.app.dao.customer;

import com.audiobea.crm.app.dao.customer.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ICustomerDao extends MongoRepository<Customer, String> {

	@Query(value = "{'name': {$regex: ?0, $options: 'i'}, 'lastName': {$regex: ?1, $options: 'i'}}")
	Page<Customer> findByNameContainsAndLastNameContains(String name, String lastName, Pageable pageable);

}
