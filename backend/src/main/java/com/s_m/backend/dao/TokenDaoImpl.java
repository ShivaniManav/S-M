package com.s_m.backend.dao;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenDaoImpl implements TokenDao {
	
	private static final String TOKEN_BLACKLIST_KEY_PREFIX = "sm::tbl::";
	private static final String PWD_RESET_TOKEN_KEY_PREFIX = "sm::prt::";
	
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
	
	@Override
	public void generatePasswordResetToken(String email, String token) {
		
		redisTemplate.opsForValue().set(getPasswordResetTokenKey(token), email, 5*60*60, TimeUnit.SECONDS);
	}

	@Override
	public String getEmailByPasswordResetToken(String token) {
		
		return (String) redisTemplate.opsForValue().get(getPasswordResetTokenKey(token));
	}

	@Override
	public void deletePasswordResetToken(String token) {
		
		redisTemplate.delete(getPasswordResetTokenKey(token));
	}

	private String getTokenBlacklistKey(String token) {
		
		String key = TOKEN_BLACKLIST_KEY_PREFIX + token;
		
		return key;
	}
	
	private String getPasswordResetTokenKey(String token) {
		
		String key = PWD_RESET_TOKEN_KEY_PREFIX + token;
		
		return key;
	}

}
