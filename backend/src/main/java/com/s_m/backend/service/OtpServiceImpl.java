package com.s_m.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.s_m.backend.dao.OtpDao;

@Service
public class OtpServiceImpl implements OtpService {

	@Autowired
	private OtpDao otpDao;
	
	@Override
	public String getOTP(String email, String otp) {
		
		return otpDao.getOTP(email, otp);
	}

	@Override
	public int generateOTP(String email) {
		
		return otpDao.generateOTP(email);
	}

	@Override
	public void invalidateOTP(String email, String otp) {
		
		otpDao.invalidateOTP(email, otp);
	}

}
