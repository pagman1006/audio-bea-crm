package com.audiobea.crm.app.business;

import java.util.List;

import com.audiobea.crm.app.dao.user.model.User;

public interface IUserService {
	
	public List<User> getUsers();
	public boolean saveUser(User user);
	public boolean updateUser(Long id, User user);
	public boolean deleteUserById(Long id);

}
