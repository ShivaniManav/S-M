package com.s_m.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
public class Welcome {
	
	@PostMapping("/hello")
	public String message() {
		return "welcome";
	}
	
}
