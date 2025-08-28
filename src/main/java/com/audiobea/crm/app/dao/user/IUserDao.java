package com.audiobea.crm.app.dao.user;

import com.audiobea.crm.app.dao.user.model.Role;
import com.audiobea.crm.app.dao.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUserDao extends MongoRepository<User, String> {

    User findByUsername(String username);

    Page<User> findByUsernameContainsIgnoreCaseAndRolesContains(String username, Role roles, Pageable pageable);

    Page<User> findByUsernameContainsIgnoreCase(String username, Pageable pageable);

    Page<User> findByRolesContains(Role role, Pageable pageable);

}