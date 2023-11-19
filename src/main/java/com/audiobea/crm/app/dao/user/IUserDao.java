package com.audiobea.crm.app.dao.user;

import com.audiobea.crm.app.dao.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUserDao extends MongoRepository<User, String> {

	User findByUsername(String username);

}
