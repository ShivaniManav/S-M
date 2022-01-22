package com.s_m.backend.dao;

import com.s_m.backend.entity.User;

public interface UserDao {
	
	User findByUserName(String userName);
    
    void save(User user);
	
}
