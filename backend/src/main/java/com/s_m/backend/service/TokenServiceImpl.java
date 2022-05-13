package com.s_m.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.s_m.backend.dao.TokenDao;

@Service
public class TokenServiceImpl implements TokenService {
	
	@Autowired
	private TokenDao tokenDao;

	@Override
	public String getTokenFromBlacklist(String token) {
		
		return tokenDao.getTokenFromBlacklist(token);
	}

	@Override
	public void addTokenInBlacklist(String token, long ttl) {
		
		tokenDao.addTokenInBlacklist(token, ttl);
	}

	@Override
	public boolean isTokenInBlacklist(String token) {
		
		return getTokenFromBlacklist(token) == null ? false : true;
	}

}
