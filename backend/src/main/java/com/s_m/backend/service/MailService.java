package com.s_m.backend.service;

public interface MailService {
	
	public void sendSimpleMessage();
	
	public void sendOtpEmail(String otp, String toEmail) throws Exception;
	
}
