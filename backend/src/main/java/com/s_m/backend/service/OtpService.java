package com.s_m.backend.service;

public interface OtpService {

	public String getOTP(String email, String otp);

	public int generateOTP(String email);
	
	public void invalidateOTP(String email, String otp);
	
}
