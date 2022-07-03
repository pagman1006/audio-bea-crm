package com.audiobea.crm.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.model.user.Role;

public interface IRoleDao extends CrudRepository<Role, Long> {
	
	Role findByAuthority(String authority);

}
