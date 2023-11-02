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
import com.audiobea.crm.app.dao.user.model.Role;
import com.audiobea.crm.app.dao.user.model.User;
import com.audiobea.crm.app.utils.Utils;
import com.audiobea.crm.app.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final MessageSource messageSource;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IRoleDao roleDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ResponseData<DtoInUser> getUsers(final String username, final String role, final Integer page, final Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("username"));
        Page<User> pageUser = userDao.findBYUsernameContainsAndRolesAuthorityContains(username, role, pageable);
        Validator.validatePage(pageUser, messageSource);
        return new ResponseData<>(pageUser.getContent().stream().map(p -> userMapper.userToDtoInUser(p)).collect(Collectors.toList()), pageUser);
    }

    @Transactional
    @Override
    public DtoInUser saveUser(DtoInUser userToSave) {
        try {
            return userMapper.userToDtoInUser(userDao.save(userMapper.userDtoInToUser(userToSave)));
        } catch (Exception e) {
            throw new DuplicateRecordException(Utils.getLocalMessage(messageSource, I18Constants.DUPLICATE_KEY.getKey(), userToSave.getUsername()));
        }
    }

    @Override
    public DtoInUser getUserById(Long id) {
        return userMapper.userToDtoInUser(userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id)))));
    }

    @Transactional
    @Override
    public DtoInUser updateUser(Long id, DtoInUser userToSave) {
        User user = userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id))));
        user.setPassword(userToSave.getPassword());
        for (Role role : user.getRoles()) {
            roleDao.delete(role);
        }

        user.setRoles(userToSave.getRoles().stream().map(r -> roleMapper.roleDtoInToRole(r)).collect(Collectors.toList()));
        return userMapper.userToDtoInUser(userDao.save(user));
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        User user = userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(id))));
        userDao.delete(user);
    }

}
