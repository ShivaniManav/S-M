package com.s_m.backend.dao;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OtpDaoImpl implements OtpDao {

	private static final String OTP_KEY_PREFIX = "sm::otp::";
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public String getOTP(String email, String otp) {
		
		return String.valueOf(redisTemplate.opsForValue().get(getOtpKey(email, otp)));
	}

	@Override
	public int generateOTP(String email) {
		
		Random random = new Random();
		int otp = 100000 + random.nextInt(999999);
		
		redisTemplate.opsForValue().set(getOtpKey(email, String.valueOf(otp)), String.valueOf(otp), 2*60, TimeUnit.SECONDS);
		
		return otp;
	}

	@Override
	public void invalidateOTP(String email, String otp) {
		
		redisTemplate.delete(getOtpKey(email, otp));
	}
	
	private String getOtpKey(String email, String otp) {
		
		return OTP_KEY_PREFIX + email + "::" + otp;
	}

}
