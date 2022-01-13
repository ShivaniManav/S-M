package com.s_m.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
public class Welcome {
	
	@GetMapping("/hello")
	public String message() {
		return "welcome";
	}
	
}
