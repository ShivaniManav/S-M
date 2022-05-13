package com.s_m.backend.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.s_m.backend.entity.User;
import com.s_m.backend.entity.UserAddress;

public interface UserService extends UserDetailsService {
	
	User findByUserName(String userName);
	
	void updateDefaultAddress(int userId, int addrId);
	
	void saveUserAddress(UserAddress userAddress);
	
	void save(User user);
}
