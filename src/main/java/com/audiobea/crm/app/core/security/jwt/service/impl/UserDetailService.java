package com.audiobea.crm.app.core.security.jwt.service.impl;

import com.audiobea.crm.app.dao.user.IUserDao;
import com.audiobea.crm.app.dao.user.model.Role;
import com.audiobea.crm.app.dao.user.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.audiobea.crm.app.core.exception.ConstantsError.LOGIN_ERROR_NOT_EXIST;
import static com.audiobea.crm.app.core.exception.ConstantsError.LOGIN_ERROR_ROLES;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_ROLE;

@Slf4j
@AllArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

	private final IUserDao userDao;

    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);

		if (user == null) {
			log.error(LOGIN_ERROR_NOT_EXIST, username);
			throw new UsernameNotFoundException("Login Error: Username '" + username + "' not exist!");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();

		for (Role role : user.getRoles()) {
			log.debug(LOG_ROLE, role.getAuthority());
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}

		if (authorities.isEmpty()) {
			log.error(LOGIN_ERROR_ROLES, username);
			throw new UsernameNotFoundException("Error en el Login: user '" + username + "' don't have roles!");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true,
				true, authorities);
	}

}
