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
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@AllArgsConstructor
@Slf4j
@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    private MessageSource messageSource;
    private IUserDao userDao;
    private IRoleDao roleDao;
    private UserMapper userMapper;
    private RoleMapper roleMapper;

    @Override
    public ResponseData<DtoInUser> getUsers(final String username, final String role, final Integer page,
            final Integer pageSize) {
        final Pageable pageable = PageRequest.of(page, pageSize, Sort.by("username"));
        Page<User> pageUser;
        log.debug("UserServiceImpl: looking for username: {}, role: {}", username, role);
        if (StringUtils.isNotBlank(role)) {
            final Role roleToFind = roleDao.findByAuthority(role.toUpperCase()).orElseThrow(() -> new NoSuchElementFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEMS_FOUND.getKey())));
            if (StringUtils.isNotBlank(username)) {
                pageUser = userDao.findByUsernameContainsIgnoreCaseAndRolesContains(username, roleToFind, pageable);
            } else {
                pageUser = userDao.findByRolesContains(roleToFind, pageable);
            }
        } else if (StringUtils.isNotBlank(username)) {
            pageUser = userDao.findByUsernameContainsIgnoreCase(username, pageable);
        } else {
            pageUser = userDao.findAll(pageable);
        }
        Validator.validatePage(pageUser, messageSource);
        return new ResponseData<>(pageUser.getContent().stream().map(userMapper::userToDtoInUser).toList(), pageUser);
    }

    @Transactional
    @Override
    public DtoInUser saveUser(final DtoInUser userToSave) {
        try {
            final User user = userMapper.userDtoInToUser(userToSave);
            final List<Role> roles = new ArrayList<>();
            user.getRoles().forEach(r -> {
                log.debug("Role: {}", r);
                Role role = roleDao.findByAuthority(r.getAuthority()).orElse(null);
                role = role != null ? role : roleDao.save(r);
                log.debug("Role: {}", role);
                roles.add(role);
            });
            user.setRoles(roles);
            return userMapper.userToDtoInUser(userDao.save(user));
        } catch (Exception e) {
            throw new DuplicateRecordException(Utils.getLocalMessage(messageSource, I18Constants.DUPLICATE_KEY.getKey(),
                    userToSave.getUsername()));
        }
    }

    @Override
    public DtoInUser getUserById(final String id) {
        return userMapper.userToDtoInUser(userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), id))));
    }

    @Transactional
    @Override
    public DtoInUser updateUser(final String id, final DtoInUser userToSave) {
        final User user = userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), id)));
        user.setPassword(userToSave.getPassword());
        roleDao.deleteAll(user.getRoles());
        user.setRoles(userToSave.getRoles().stream().map(roleMapper::roleDtoInToRole).toList());
        return userMapper.userToDtoInUser(userDao.save(user));
    }

    @Transactional
    @Override
    public void deleteUserById(final String id) {
        final User user = userDao.findById(id).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), id)));
        userDao.delete(user);
    }

}
