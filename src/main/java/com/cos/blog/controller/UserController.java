package com.cos.blog.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class UserController {

	// 인증이 안된 사용자들의 출입 허용
	// 1. 매핑된 /auth/ 이하의 경로
	// 2. url 경로가 "/"
	// 3. static 폴더 하위에 있는 /js/**, /css/**, /image/** ...
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/auth/joinForm")	// header.jsp 에서 받아온 url 경로 
	public String joinForm() {
		return "user/joinForm";				// jsp 경로
	}

	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm() {
		return "user/updateForm";
	}
	
	@GetMapping("auth/kakao/callback")
	public @ResponseBody String kakaoCallback(String code) {	// @ResponseBody : Data를 리턴해주는 컨트롤러 함수
		
		// 1. 카카오 토큰 받기
		// POST 방식으로 key-value 데이터를 카카오에 요청
		RestTemplate rt = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "7481d3f8938ab995972608ade8c364bd");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);
		
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		// - kakaoTokenRequest가 headers와 params의 데이터를 가지게 된다
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<>(params, headers);
		
		// Http 요청하기 (POST 방식) - response 변수의 응답을 받음
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);
		
		// Json 데이터를 담을 수 있는 라이브러리 :  Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		
		try {
			oauthToken =  objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
 			e.printStackTrace();
		}
		
		System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());
		
		// 2. 사용자 정보 요청
		// POST 방식으로 key-value 데이터를 카카오에 요청
		RestTemplate rt2 = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		// - kakaoTokenRequest가 headers와 params의 데이터를 가지게 된다
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
				new HttpEntity<>(headers2);
		
		// Http 요청하기 (POST 방식) - response 변수의 응답을 받음
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
		);
		
		System.out.println("카카오 요청 : " + response2.getBody());
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakapProfile = null;
		
		try {
			kakapProfile =  objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
 			e.printStackTrace();
		}
		
		// 필요한 User 오브젝트 정보 : username, password, email
		System.out.println("카카오 아이디 : " + kakapProfile.getId());
		System.out.println("카카오 이메일 : " + kakapProfile.getKakao_account().getEmail());
		
		System.out.println("(int)(Math.random() * 1000) + 1) : " + (int)(Math.random() * 1000) + 1);
		
		System.out.println("블로그 서버 유저네임 : " + "blog_" + kakapProfile.getId());
		System.out.println("블로그 서버 이메일 : " + "blog_" + ((int)(Math.random() * 1000) + 1));
		UUID garbagePassword = UUID.randomUUID();
		System.out.println("블로그 서버 패스워드   : " + garbagePassword);
		
		// 카카오 로그인시 블로그 서버에 회원가입(자동 가입)
		User kakaoUser = User.builder()
				.username("blog_" + kakapProfile.getId())
				.email("blog_" + ((int)(Math.random() * 1000) + 1))
				.password(garbagePassword.toString())
				.build();
		
		System.out.println("1111111111111111111111111111");
		System.out.println("카카오 유저네임 : " + kakaoUser.getUsername());
		// 회원, 비회원 체크
		User originUser = userService.회원찾기(kakaoUser.getUsername());
		
		if(originUser.getUsername() == null) {			// 비회원일 경우
			System.out.println("기존 회원이 아닙니다");
			userService.회원가입(kakaoUser);					// - 회원가입하기
		}
		System.out.println("2222222222222222222222222");
		// 로그인 처리 (=세션 등록)
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), kakaoUser.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		System.out.println("3333333333333333333333333");
		return "redirect:/";
	}
}
