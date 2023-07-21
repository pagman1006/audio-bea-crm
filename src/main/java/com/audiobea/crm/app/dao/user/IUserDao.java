package com.audiobea.crm.app.dao.user;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.user.model.User;

public interface IUserDao extends CrudRepository<User, Long> {
	
	User findByUsername(String username);

}
