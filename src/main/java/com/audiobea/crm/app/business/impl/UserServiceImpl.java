package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.IUserService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.commons.mapper.RoleMapper;
import com.audiobea.crm.app.commons.mapper.UserMapper;
import com.audiobea.crm.app.core.exception.DuplicateRecordException;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.dao.user.IRoleDao;
import com.audiobea.crm.app.dao.user.IUserDao;
import com.audiobea.crm.app.dao.user.model.User;
import com.audiobea.crm.app.utils.Utils;
import com.audiobea.crm.app.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@AllArgsConstructor
@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

	private MessageSource messageSource;
	private IUserDao userDao;
	private IRoleDao roleDao;
	private UserMapper userMapper;
	private RoleMapper roleMapper;


	@Override
	public ResponseData<DtoInUser> getUsers(final String username, final String role, final Integer page, final Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("username"));
		Page<User> pageUser = userDao.findAll(pageable);
		//userDao.findBYUsernameContainsAndRolesAuthorityContains(username, role, pageable);
		Validator.validatePage(pageUser, messageSource);
		return new ResponseData<>(pageUser.getContent().stream().map(p -> userMapper.userToDtoInUser(p)).collect(Collectors.toList()), pageUser);
	}

	@Transactional
	@Override
	public DtoInUser saveUser(DtoInUser userToSave) {
		try {
			User user = userMapper.userDtoInToUser(userToSave);
			roleDao.saveAll(user.getRoles());
			return userMapper.userToDtoInUser(userDao.save(user));
		} catch (Exception e) {
			throw new DuplicateRecordException(Utils.getLocalMessage(messageSource, I18Constants.DUPLICATE_KEY.getKey(), userToSave.getUsername()));
		}
	}

	@Override
	public DtoInUser getUserById(String id) {
		return userMapper.userToDtoInUser(userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), id))));
	}

	@Transactional
	@Override
	public DtoInUser updateUser(String id, DtoInUser userToSave) {
		User user = userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), id)));
		user.setPassword(userToSave.getPassword());
		roleDao.deleteAll(user.getRoles());

		user.setRoles(userToSave.getRoles().stream().map(r -> roleMapper.roleDtoInToRole(r)).collect(Collectors.toList()));
		return userMapper.userToDtoInUser(userDao.save(user));
	}

	@Transactional
	@Override
	public void deleteUserById(String id) {
		User user = userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), id)));
		userDao.delete(user);
	}

}
