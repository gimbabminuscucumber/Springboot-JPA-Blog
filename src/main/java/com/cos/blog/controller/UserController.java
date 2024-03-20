package com.cos.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

	@GetMapping("/user/joinForm")	// header.jsp 에서 받아온 url 경로 
	public String joinForm() {
		return "user/joinForm";				// jsp 경로
	}

	@GetMapping("/user/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
}
