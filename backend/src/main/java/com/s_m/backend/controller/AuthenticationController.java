package com.s_m.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.s_m.backend.config.model.JwtAuthResponse;
import com.s_m.backend.config.model.LoginRequest;
import com.s_m.backend.config.utility.JWTUtil;
import com.s_m.backend.entity.User;
import com.s_m.backend.service.UserService;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/register")
	public String registerUser(@RequestBody User registrationRequest) throws Exception {

		try {
			
			userService.save(registrationRequest);
			
		} catch (Exception e) {
			throw new Exception("REGISTRATION FAILED", e);
		}
		return "User Registered Successfully";
	}
	
	@PostMapping("/login")
	public JwtAuthResponse loginUser(@RequestBody LoginRequest request) throws Exception {
		try {
			authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(
								request.getUsername(), 
								request.getPassword()
							)
						); 
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		
		final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
		
		final String token = jwtUtil.generateToken(userDetails);
		
		return new JwtAuthResponse(token);
	}
	
}
