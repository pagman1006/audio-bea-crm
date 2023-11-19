package com.audiobea.crm.app.core.security.jwt.business.impl;

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

@Slf4j
@AllArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

	private final IUserDao userDao;

    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);

		if (user == null) {
			log.error("Login Error: username {} not exist!", username);
			throw new UsernameNotFoundException("Login Error: Username '" + username + "' not exist!");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();

		for (Role role : user.getRoles()) {
			log.debug("Role: {}", role.getAuthority());
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}

		if (authorities.isEmpty()) {
			log.error("Login Error: User '{} don't have roles!", username);
			throw new UsernameNotFoundException("Error en el Login: user '" + username + "' don't have roles!");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true,
				true, authorities);
	}

}
