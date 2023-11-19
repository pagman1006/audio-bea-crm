package com.audiobea.crm.app.dao.user;

import com.audiobea.crm.app.dao.user.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IRoleDao extends MongoRepository<Role, String> {
	
	//Role findByAuthority(String authority);

}
