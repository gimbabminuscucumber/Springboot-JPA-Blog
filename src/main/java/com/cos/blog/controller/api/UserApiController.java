package com.cos.blog.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController							// 데이터만 리턴 해줄거라서 RestController
public class UserApiController {
	
	@Autowired								// DI 가능 (IoC를 통해서 @Service가 붙은 클래스를 스프링 bean에 등록해줌)
	private UserService userService;
	
	@Autowired								// 세션을 사용할 메소드의 매개변수에 넣어도 되지만 별도로 @Autowired로 bean 등록을 해도 됨
	private HttpSession session;
	
	@PostMapping("/api/user")
	public ResponseDto<Integer> save(@RequestBody User user) { 				// 요청받는게 JSON이니까 @RequestBody / user 객체 사용 가능해짐
		// 실제 DB에 insert하고 return 하기
		user.setRole(RoleType.USER);																		// http에서 user 객체로 받아온 데이터는 username, password, email 뿐이라서 role은 직접 추가
		userService.회원가입(user);																			// userServive.회원가입() 메소드에 http에서 받아온 매개변수 user를 넣음
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);				// Jackson에 의해 자바오브젝트를 JSON으로 변환해서 리턴 / 회원가입이 정상이면 1을 리턴 (에러는 GlobalExceptionHandler에서 처리)
	}						
	
	// 스프링 시큐리티를 사용하지 않은 로그인 방식
	@PostMapping("/api/user/login")
	public ResponseDto<Integer> login(@RequestBody User user /*, HttpSession session*/){
		System.out.println("UserApiController 로그");
		User principal = userService.로그인(user);		// principal	 : 접근주체
		
		if(principal != null) {
			session.setAttribute("principal", principal);
		}
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);	
	}
}
