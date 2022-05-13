package com.s_m.backend.service;

public interface TokenService {
	
	String getTokenFromBlacklist(String token);
	
	void addTokenInBlacklist(String token, long ttl);
	
	boolean isTokenInBlacklist(String token);

}
