package com.audiobea.crm.app.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.audiobea.crm.app.business.IUserService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.dao.user.IRoleDao;
import com.audiobea.crm.app.dao.user.IUserDao;
import com.audiobea.crm.app.dao.user.model.Role;
import com.audiobea.crm.app.dao.user.model.User;
import com.audiobea.crm.app.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.utils.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service("usuarioService")
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;

	private IRoleDao roleDao;

	private final MessageSource messageSource;

	@Override
	public Page<User> getUsers(final String username, final String role, final Integer page, final Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("username"));
		return userDao.findBYUsernameContainsAndRolesAuthorityContains(username, role, pageable);
	}

	@Transactional(readOnly = false)
	@Override
	public User saveUser(User userToSave) {
		return userDao.save(userToSave);
	}

	@Override
	public User getUserById(Long id) {
		return userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id))));
	}

	@Transactional(readOnly = false)
	@Override
	public User updateUser(Long id, User userToSave) {
		User user = userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id))));
		user.setPassword(userToSave.getPassword());
		for (Role role : user.getRoles()) {
			roleDao.delete(role);
		}
		user.setRoles(userToSave.getRoles());
		return userDao.save(user);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean deleteUserById(Long id) {
		boolean response = false;
		User user = userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id))));
		userDao.delete(user);
		response = true;
		return response;
	}

}
