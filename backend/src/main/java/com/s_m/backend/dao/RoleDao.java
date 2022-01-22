package com.s_m.backend.dao;

import com.s_m.backend.entity.Role;

public interface RoleDao {

	Role findRoleByName(String theRoleName);
	
}
