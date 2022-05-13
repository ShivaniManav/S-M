package com.s_m.backend.dao;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.s_m.backend.entity.Role;

@Repository
public class RoleDaoImpl implements RoleDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Role findRoleByName(String theRoleName) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from Role where name=:roleName", Role.class);
		query.setParameter("roleName", theRoleName);
		Role role = null;
		
		try {
			role = (Role) query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return role;
	}
	
}
