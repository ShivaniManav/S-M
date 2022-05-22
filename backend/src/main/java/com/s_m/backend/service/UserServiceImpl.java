package com.s_m.backend.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.s_m.backend.dao.RoleDao;
import com.s_m.backend.dao.UserAddressDao;
import com.s_m.backend.dao.UserDao;
import com.s_m.backend.entity.Role;
import com.s_m.backend.entity.User;
import com.s_m.backend.entity.UserAddress;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserAddressDao userAddressDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public User findByUserName(String userName) {
		
		return userDao.findByUserName(userName);
	}

	@Override
	@Transactional
	public User findByEmail(String email) {
		
		return userDao.findByEmail(email);
	}

	@Override
	@Transactional
	public void updateDefaultAddress(int userId, int addrId) {
		
		userAddressDao.updateDefaultAddress(userId, addrId);
	}

	@Override
	@Transactional
	public void save(User user) {
		
		userDao.save(user);
	}

	@Override
	@Transactional
	public void saveUserAddress(UserAddress userAddress) {
		
		userAddressDao.save(userAddress);
	}
	
}
