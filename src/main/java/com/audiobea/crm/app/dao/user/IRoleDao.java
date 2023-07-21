package com.audiobea.crm.app.dao.user;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.user.model.Role;

public interface IRoleDao extends CrudRepository<Role, Long> {
	
	Role findByAuthority(String authority);

}
