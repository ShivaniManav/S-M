package com.s_m.backend.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.s_m.backend.entity.User;
import com.s_m.backend.request.LoginRequest;
import com.s_m.backend.request.PasswordResetRequest;
import com.s_m.backend.request.RegistrationRequest;
import com.s_m.backend.response.JwtAuthResponse;
import com.s_m.backend.service.MailService;
import com.s_m.backend.service.OtpService;
import com.s_m.backend.service.TokenService;
import com.s_m.backend.service.UserService;
import com.s_m.backend.utility.CookieUtil;
import com.s_m.backend.utility.JWTUtil;

import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private CookieUtil cookieUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) throws Exception {

		Map<String, Object> response = new HashMap<>();
		
		try {
			
			User user = createUserModelFromRequest(registrationRequest);
			
			userService.save(user);
			
		} catch (Exception e) {
			throw new Exception("REGISTRATION FAILED");
		}
		
		response.put("message", "User Registered Successfully");
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
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
		
		response.addHeader("Set-Cookie", cookieUtil.generateCookieForToken(token, maxAge));
		
		return new JwtAuthResponse(token);
	}
	
	@PostMapping("/logout")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> responseEntityBody = new HashMap<>();
		
		String token = cookieUtil.getAccessTokenFromCookie(request.getCookies());
		long ttl = (jwtUtil.getExpirationDateFromToken(token).getTime()/1000) - (new Date().getTime()/1000);
		
		if(token != null) {
			tokenService.addTokenInBlacklist(token, ttl);
		}

		response.addHeader("Set-Cookie", cookieUtil.generateCookieForToken(token, Long.parseLong("0")));
		
		responseEntityBody.put("message", "User logged out successfully!");
		
		return new ResponseEntity<Map<String,Object>>(responseEntityBody, HttpStatus.OK);
	}
	
	@PostMapping("/password-reset")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> passwordReset(@RequestBody PasswordResetRequest passwordResetRequest, HttpServletRequest request) throws Exception {
		
		Map<String, Object> responseEntityBody = new HashMap<>();
		
		String token = cookieUtil.getAccessTokenFromCookie(request.getCookies());
		
		String username = jwtUtil.getUsernameFromToken(token);
		
		User user = userService.findByUserName(username);
		
		if(passwordEncoder.matches(passwordResetRequest.getOldPassword(), user.getPassword())) {
			if(passwordResetRequest.getNewPassword().equals(passwordResetRequest.getConfirmNewPassword())) {
				user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
			} else {
				throw new Exception("New password and Confirm new password fields must match!");
			}
		} else {
			throw new Exception("Invalid current password provided!");
		}
		
		userService.save(user);
		
		responseEntityBody.put("message", "Password has been resetted successfully for " + user.getUsername());
		
		return new ResponseEntity<Map<String,Object>>(responseEntityBody, HttpStatus.OK);
	}
	
	@GetMapping("/mail")
	@ResponseStatus(HttpStatus.OK)
	public void sendMail() {
		mailService.sendSimpleMessage();
	}
	
	@GetMapping("/forgot-password-email")
	@ResponseStatus(HttpStatus.OK)
	public void processForgotPasswordEmailForm(@RequestParam(name = "email") String email) throws Exception {
		User user = userService.findByEmail(email);
		if(user != null) {
			int otp = otpService.generateOTP(email);
			mailService.sendOtpEmail(String.valueOf(otp), email);
		} else {
			throw new Exception("User not found!!");
		}
	}
	
	@PostMapping("/validate-forgot-password-otp")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<?> processForgotPasswordOtpForm(@RequestBody Map<String, String> request) throws Exception {
		
		Map<String, Object> response = new HashMap<>();
		
		String clientOtp = String.valueOf(request.get("otp"));
		String serverOtp = null;
		
		if(clientOtp != null && !clientOtp.equals("")) {
			serverOtp = otpService.getOTP(request.get("email"), clientOtp);
		}
		
		if(serverOtp == null) {
			throw new Exception("Invalid OTP!");
		}
		
		otpService.invalidateOTP(request.get("email"), clientOtp);
		
		String passwordResetToken = RandomString.make(30);
		tokenService.generatePasswordResetToken(request.get("email"), passwordResetToken);
		
		response.put("isOtpValid", true);
		response.put("passwordResetToken", passwordResetToken);
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/forgot-password")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> processForgotPassword(@RequestBody Map<String, String> request, 
					@RequestParam(name = "passwordResetToken") String passwordResetToken) throws Exception {
		
		User user = null;
		Map<String, Object> response = new HashMap<>();
		
		String newPwd = request.get("newPassword");
		String confNewPwd = request.get("confirmNewPassword");
		
		String email = tokenService.getEmailByPasswordResetToken(passwordResetToken);
		
		if(email == null) {
			throw new Exception("Invalid Password Reset Token!");
		}
		
		if(newPwd.equals(confNewPwd)) {
			
			user = userService.findByEmail(email);
			user.setPassword(passwordEncoder.encode(newPwd));
			userService.save(user);
			
			tokenService.deletePasswordResetToken(passwordResetToken);
			
			response.put("message", "Password has been resetted successfully for " + user.getUsername());
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} else {
			throw new Exception("Both password fields must match!");
		}
	}
	
	public User createUserModelFromRequest(RegistrationRequest registrationRequest) {
		
		User user = new User();
		
		user.setUsername(registrationRequest.getUsername());
		user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		user.setFirstName(registrationRequest.getFirstName());
		user.setLastName(registrationRequest.getLastName());
		user.setEmail(registrationRequest.getEmail());
		user.setMobile(registrationRequest.getMobile());
		user.setRoles(registrationRequest.getRoles());
		user.setActive(1);
		user.setCreatedAt(new Date());
		user.setModifiedAt(new Date());
		
		return user;
	}
	
}
