package com.s_m.backend.config.model;

public class JwtAuthResponse {
	
	private String jwtToken;

	public JwtAuthResponse() {
		
	}

	public JwtAuthResponse(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
}
