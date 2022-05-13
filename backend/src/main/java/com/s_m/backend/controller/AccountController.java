package com.s_m.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.s_m.backend.entity.User;
import com.s_m.backend.entity.UserAddress;
import com.s_m.backend.service.UserService;

//@CrossOrigin(origins = "http://127.0.0.1:5501",allowedHeaders = "*")
@RestController
@RequestMapping("account")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/details")
	public User getUser(@RequestParam String username) throws Exception {
		try {
			User theUser = userService.findByUserName(username);
			
			return theUser;
		} catch (Exception e) {
			throw new Exception("No such user was found");
		}
	}
	
	@PostMapping("/details")
	public String updateDetails() throws Exception {
		
		
		return "";
	}
	
	@PostMapping("/address")
	public String addUserAddress(@RequestParam String username, @RequestBody UserAddress userAddress) {
		try {
			// User theUser = userService.findByUserName(username);
			// userAddress.setFkUser(theUser);
			userService.saveUserAddress(userAddress);

			return "Address saved successfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "bad request";
		}
		
	}
	
	@PostMapping("/address/update-default")
	public String updatedDefaultAddress(@RequestParam("user_id") int userId, @RequestParam("addr_id") int addrId) throws Exception {
		try {
			userService.updateDefaultAddress(userId, addrId);
			return "default address was updated successfully";
		} catch (Exception e) {
			throw new Exception("No such user was found");
		}
	}
	
}
