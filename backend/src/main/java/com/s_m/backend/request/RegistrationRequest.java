package com.s_m.backend.request;

public class RegistrationRequest {
	
	private String username;
	
	private String password;
	
	private String mobile;
	
	private String email;
	
	private String first_name;
	
	private String last_name;
	
	public RegistrationRequest() {
		
	}

	public RegistrationRequest(String username, String password, String mobile, String email, String first_name,
			String last_name) {
		this.username = username;
		this.password = password;
		this.mobile = mobile;
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
}
