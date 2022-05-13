package com.s_m.backend.controller;


import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.s_m.backend.entity.User;
import com.s_m.backend.request.LoginRequest;
import com.s_m.backend.response.JwtAuthResponse;
import com.s_m.backend.service.TokenService;
import com.s_m.backend.service.UserService;
import com.s_m.backend.utility.JWTUtil;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;
	
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public String registerUser(@RequestBody User registrationRequest) throws Exception {

		try {
			
			userService.save(registrationRequest);
			
		} catch (Exception e) {
			throw new Exception("REGISTRATION FAILED", e);
		}
		return "User Registered Successfully";
	}
	
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public JwtAuthResponse loginUser(@RequestBody LoginRequest request, HttpServletResponse response) throws Exception {
		try {

			authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(
								request.getUsername(), 
								request.getPassword()
							)
						); 
			
		} catch (AuthenticationException e) {
			throw e;
		}
		
		final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
			
		final String token = jwtUtil.generateToken(userDetails);

		long maxAge = jwtUtil.getExpirationDateFromToken(token).getTime()/1000;
		
		String cookie = "access_token="+token+";Max-Age="+maxAge+";Path=/;HttpOnly=true;SameSite=None;Secure=false";
		response.addHeader("Set-Cookie", cookie);
		
		return new JwtAuthResponse(token);
	}
	
	@PostMapping("/logout")
	@ResponseStatus(HttpStatus.OK)
	public void logoutUser(HttpServletRequest request, HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();
		
		String token = null;
		long ttl = 0;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("access_token")) {
					token = cookie.getValue();
					ttl = (jwtUtil.getExpirationDateFromToken(token).getTime()/1000) - (new Date().getTime()/1000);
					System.out.println(ttl);
				}
			}
		}
		
		if(token != null) {
			tokenService.addTokenInBlacklist(token, ttl);
		}

		String cookie = "access_token="+""+";Max-Age="+(0)+";Path=/;HttpOnly=true;SameSite=None;Secure=false";
		response.addHeader("Set-Cookie", cookie);
	}
	
//	@PostMapping("/password-reset")
//	@ResponseStatus(HttpStatus.CREATED)
//	public void passwordReset() {
//		
//	}
}
