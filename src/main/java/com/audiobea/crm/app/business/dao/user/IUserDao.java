package com.audiobea.crm.app.business.dao.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.user.model.User;

public interface IUserDao extends PagingAndSortingRepository<User, Long> {

	User findByUsername(String username);

	@Query(value = "SELECT u.* FROM users u LEFT JOIN authorities a ON u.user_id = a.user_id WHERE (u.username like %:username%) AND (a.authority like %:role%)", nativeQuery = true)
	Page<User> findBYUsernameContainsAndRolesAuthorityContains(String username, String role, Pageable pageable);

}
