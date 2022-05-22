package com.s_m.backend.dao;

import com.s_m.backend.entity.User;

public interface UserDao {
	
	User findByUserName(String userName);
	
	User findByEmail(String email);
    
    void save(User user);
	
}
