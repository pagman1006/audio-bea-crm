package com.audiobea.crm.app.business;

import org.springframework.data.domain.Page;

import com.audiobea.crm.app.business.dao.user.model.User;

public interface IUserService {

	public Page<User> getUsers(String username, String role, Integer page, Integer pageSize);

	public User saveUser(User user);

	public User getUserById(Long id);

	public User updateUser(Long id, User user);

	public boolean deleteUserById(Long id);

}
