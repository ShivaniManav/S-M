package com.s_m.backend.dao;

public interface TokenDao {
	
	String getTokenFromBlacklist(String token);
	
	void addTokenInBlacklist(String token, long ttl);
	
}
