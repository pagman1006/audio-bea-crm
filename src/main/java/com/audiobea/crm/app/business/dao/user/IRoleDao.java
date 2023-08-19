package com.audiobea.crm.app.business.dao.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.user.model.Role;

public interface IRoleDao extends PagingAndSortingRepository<Role, Long> {
	
	Role findByAuthority(String authority);

}
