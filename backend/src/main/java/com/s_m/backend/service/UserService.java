package com.s_m.backend.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.s_m.backend.entity.User;

public interface UserService extends UserDetailsService {
	
	User findByUserName(String userName);
	
	void save(User user);
}
