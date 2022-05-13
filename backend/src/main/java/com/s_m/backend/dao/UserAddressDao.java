package com.s_m.backend.dao;

import java.util.Collection;

import com.s_m.backend.entity.User;
import com.s_m.backend.entity.UserAddress;

public interface UserAddressDao {
	
	UserAddress findDefaultAddressByUserId(int userId);
	
	Collection<UserAddress> getAllUserAddressesByUserId(int userId);
	
	void updateDefaultAddress(int userId, int addrId);
	
	void save(UserAddress userAddress);
	
}
