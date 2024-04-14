package com.cos.blog.controller.api;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;


@RestController							// 데이터만 리턴 해줄거라서 RestController
public class UserApiController {
	
	@Autowired								// DI 가능 (IoC를 통해서 @Service가 붙은 클래스를 스프링 bean에 등록해줌)
	private UserService userService;
	
	@Autowired								// 세션을 사용할 메소드의 매개변수에 넣어도 되지만 별도로 @Autowired로 bean 등록을 해도 됨
	private HttpSession session;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) { 				// 요청받는게 JSON이니까 @RequestBody / user 객체 사용 가능해짐
		// 실제 DB에 insert하고 return 하기																// - JSON이 아닌 key-value 타입으로 데이터를 받으려면 @RequestBody 안 쓰면 됨
		System.out.println("UserApiController : save 호출됨");
		userService.회원가입(user);																			// userServive.회원가입() 메소드에 http에서 받아온 매개변수 user를 넣음
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);				// Jackson에 의해 자바오브젝트를 JSON으로 변환해서 리턴 / 회원가입이 정상이면 1을 리턴 (에러는 GlobalExceptionHandler에서 처리)
	}						
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user){
		userService.회원수정(user);

		// 여기서는 트랜잭션이 종료되기 때문에 DB에 값은 변경이 됐음
		// 하지만, 세션값은 변경되지 않은 상태이기 때문에 개발자가 직접 세션값을 변경해야 함

		// 세션 등록
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	// 스프링 시큐리티를 사용하지 않은 로그인 방식
//	@PostMapping("/api/user/login")
//	public ResponseDto<Integer> login(@RequestBody User user/*, HttpSession session*/){	// 매개변수에 HttpSession은 @Auto-wired 어노테이션써서 별도로 빼도 됨
//		System.out.println("UserApiController 로그인");
//		User principal = userService.로그인(user);				// principal	 : 접근주체
//		
//		if(principal != null) {
//			session.setAttribute("principal", principal);		// session에 "principal"이라는 변수에 principal 객체를 담겠다
//		}
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);	
//	}
	
}
