package com.s_m.backend.dao;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenDaoImpl implements TokenDao {
	
	private static final String TOKEN_BLACKLIST_KEY_PREFIX = "sm::tbl::";
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Override
	public String getTokenFromBlacklist(String token) {
		
		return (String) redisTemplate.opsForValue().get(getTokenBlacklistKey(token));
	}

	@Override
	public void addTokenInBlacklist(String token, long ttl) {
		
		redisTemplate.opsForValue().set(getTokenBlacklistKey(token), token, ttl, TimeUnit.SECONDS);
	}
	
	private String getTokenBlacklistKey(String token) {
		
		String key = TOKEN_BLACKLIST_KEY_PREFIX + token;
		
		return key;
	}

}
