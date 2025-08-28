package com.audiobea.crm.app.dao.user;

import com.audiobea.crm.app.dao.user.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IRoleDao extends MongoRepository<Role, String> {
	
	Optional<Role> findByAuthority(String authority);

}
