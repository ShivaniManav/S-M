package com.s_m.backend.config.model;

public class LoginRequest {
	
	//TODO: Try to implement authentication using email
	
	private String username;
	
	private String password;
	
	public LoginRequest() {
		
	}

	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
