package com.s_m.backend.dao;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.s_m.backend.entity.User;
import com.s_m.backend.entity.UserAddress;

@Repository
public class UserAddressDaoImpl implements UserAddressDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public UserAddress findDefaultAddressByUserId(int userId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<UserAddress> query = session.createQuery("from user_address where default=1 and user_id=:userId", UserAddress.class);
		query.setParameter("userId", userId);
		
		UserAddress userAddress = query.getSingleResult();
		
		return userAddress;
	}

	@Override
	public Collection<UserAddress> getAllUserAddressesByUserId(int userId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<UserAddress> query = session.createQuery("from user_address where user_id=:userId", UserAddress.class);
		query.setParameter("userId", userId);
		
		ArrayList<UserAddress> userAddresses = (ArrayList<UserAddress>)query.getResultList();
		
		return userAddresses;
	}

	@Override
	public void updateDefaultAddress(int userId, int addrId) {
		Session session = sessionFactory.getCurrentSession();
		
		StoredProcedureQuery query = session.createStoredProcedureQuery("updateDefaultAddress")
											.registerStoredProcedureParameter("addr_id", Integer.class, ParameterMode.IN)
											.registerStoredProcedureParameter("user_id", Integer.class, ParameterMode.IN)
											.setParameter("addr_id", addrId)
											.setParameter("user_id", userId);
		
		query.execute();
	}

	@Override
	public void save(UserAddress userAddress) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(userAddress);
	}
	
}
