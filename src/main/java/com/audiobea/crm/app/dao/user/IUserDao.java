package com.audiobea.crm.app.dao.user;

import com.audiobea.crm.app.dao.user.model.User;
import com.audiobea.crm.app.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface IUserDao extends PagingAndSortingRepository<User, Long>, CrudRepository<User, Long> {

	User findByUsername(String username);

	@Query(value = Constants.FIND_USERS_BY_USERNAME_ROLES, nativeQuery = true)
	Page<User> findBYUsernameContainsAndRolesAuthorityContains(@Param("username") String username, @Param("role") String role, Pageable pageable);

}
