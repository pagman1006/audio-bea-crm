package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInUser;

public interface IUserService {

	ResponseData<DtoInUser> getUsers(String username, String role, Integer page, Integer pageSize);

	DtoInUser saveUser(DtoInUser user);

	DtoInUser getUserById(Long id);

	DtoInUser updateUser(Long id, DtoInUser user);

	void deleteUserById(Long id);

}
