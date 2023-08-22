package com.audiobea.crm.app.business.dao.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.user.model.User;
import com.audiobea.crm.app.utils.Constants;

public interface IUserDao extends PagingAndSortingRepository<User, Long> {

	User findByUsername(String username);

	@Query(value = Constants.FIND_USERS_BY_USERNAME_ROLES, nativeQuery = true)
	Page<User> findBYUsernameContainsAndRolesAuthorityContains(String username, String role, Pageable pageable);

}
