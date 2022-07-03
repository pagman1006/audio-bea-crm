package com.audiobea.crm.app.business.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audiobea.crm.app.business.IUserService;
import com.audiobea.crm.app.dao.IUserDao;
import com.audiobea.crm.app.dao.model.user.User;

@Service("usuarioService")
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserDao usuarioDao;

	@Override
	public List<User> getUsers() {
		return StreamSupport.stream(usuarioDao.findAll().spliterator(), false)
				.collect(Collectors.toList());
	}
	
	@Override
	public boolean saveUser(User userToSave) {
		User user = usuarioDao.findByUsername(userToSave.getUsername());
		
		if ( user != null ) {
			return false;
		} else {
			usuarioDao.save(userToSave);
			return true;
		}
	}

	@Override
	public boolean updateUser(Long id, User userToSave) {
		boolean response = false;
		User user = usuarioDao.findById(id).orElse(null);
		if (user != null) {
			System.out.println("User: " + user.toString());
		} else {
			System.out.println("User To Update: " + userToSave.toString());
		}
		if (user != null) {
			user.setPassword(userToSave.getPassword());
			user.setRoles(userToSave.getRoles());
			usuarioDao.save(user);
			response = true;
		}
		return response;
	}

	@Override
	public boolean deleteUserById(Long id) {
		boolean response = false;
		usuarioDao.deleteById(id);
		response = true;
		return response;
	}

}
