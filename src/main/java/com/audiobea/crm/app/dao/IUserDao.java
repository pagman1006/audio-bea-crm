package com.audiobea.crm.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.audiobea.crm.app.dao.model.user.User;

public interface IUserDao extends CrudRepository<User, Long> {
	
	User findByUsername(String username);

}
