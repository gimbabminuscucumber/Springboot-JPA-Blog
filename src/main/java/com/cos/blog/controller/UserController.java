package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 안된 사용자들의 출입 허용
// 1. 매핑된 /auth/ 이하의 경로
// 2. url 경로가 "/"
// 3. static 폴더 하위에 있는 /js/**, /css/**, /image/** ...

@Controller
public class UserController {

	@Value("${cos.key}")
	private String cosKey;
	
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
	
	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) {		// @ResponseBody : Data를 리턴해주는 컨트롤러 함수
		
		// 1. 카카오 토큰 받기
		// 1-1. POST 방식으로 key-value 데이터를 카카오에 요청
		// - <a>태그는 get방식
		RestTemplate rt = new RestTemplate();
		
		// 1-2. HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");	// content-type :  내가 전송할 http body	데이터가 key-value 형태의 데이터이다
		
		// 1-3. HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "7481d3f8938ab995972608ade8c364bd");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);
		
		//1-4. HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		// - kakaoTokenRequest가 headers와 params의 데이터를 가지게 된다
		// - 하기 메소드 exchange가 HttpEntity를 받기 때문에 작성한 코드
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = 
				new HttpEntity<>(params, headers);
		
		// 1-5. Http 요청하기 (POST 방식) 
		// - response 변수의 응답을 받음
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,			// 요청 메소드
				kakaoTokenRequest,			// httpBody와 header 값
				String.class							// 응답을 받을 타입
		);
		
//		System.out.println("토큰 요청에 대한 응답 : " + response);
//		System.out.println("토큰 요청에 대한 응답(body값) : " + response.getBody());
//		System.out.println("토큰 요청에 대한 응답(header값) : " + response.getHeaders());
		
		// 1-6. Json 데이터를 오브젝트에 담는 라이브러리 :  Gson, Json Simple, ObjectMapper
		// - Json 데이터를 자바로 처리하기 위해 자바 오브젝트에 담음
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		
			try {
				oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);	// response.getBody()를  OAuthToken.class에 담음
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}	
			
		// response.getBody()를 OAuthToken.class에 담음
		// Json 데이터를 Java로 처리하기 위해 Java 오브젝트로 바꾼 것 
//		System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());
		
		// 2. 사용자 정보 요청
		// 2-1. POST 방식으로 key-value 데이터를 카카오에 요청
		RestTemplate rt2 = new RestTemplate();
		
		// 2-2. HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 2-3. HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		// - kakaoTokenRequest가 headers와 params의 데이터를 가지게 된다
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = 
				new HttpEntity<>(headers2);
		
		// 2-4. Http 요청하기 (POST 방식) - response 변수의 응답을 받음
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
		);
		
//		System.out.println("카카오 요청 : " + response2.getBody());
		
		// 2-5. Json 데이터를 오브젝트에 담기
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
 			e.printStackTrace();
		}
		
		// 필요한 User 오브젝트 정보 : username, password, email
		/*
		System.out.println("카카오 아이디(번호) : " + kakaoProfile.getId());
		System.out.println("카카오 이메일 : " + kakaoProfile.getKakao_account().getEmail());	// email을 받을 수가 없어서 null값이 온다
		
		System.out.println("카카오 로그인 유저네임 : " + kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
		System.out.println("카카오 로그인 이메일 : " + kakaoProfile.getKakao_account().getEmail()+"@blog.com");
		System.out.println("카카오 로그인 패스워드   : " + cosKey);
		 */
		
//		// UUID : 중복되지 않는 어떤 특정한 값을 만들어내는 알고리즘
//		UUID garbagePassword = UUID.randomUUID();
//		System.out.println("블로그 서버 패스워드   : " + garbagePassword);
		
		// 3. 카카오 로그인
		// 3-1. 카카오 로그인시 블로그 서버에 회원가입(자동 가입)
		// - period(.)로 사용하는 것들이 DB에 항목으로 저장됨
		User kakaoUser = User.builder()
				.username("kakao_"+kakaoProfile.getId())
				.password(cosKey)										// yml에 저장한 cos1234
				.email("kakao_"+kakaoProfile.getId()+"@blog.com")
				.oauth("kakao")
				.build();
		
		// 3-2. 회원, 비회원 체크
		// - 비회원 > 회원가입 
		// - 회원 > 로그인
		User originUser = userService.회원찾기(kakaoUser.getUsername());
		
		if(originUser.getUsername() == null) {			// 비회원일 경우
			userService.회원가입(kakaoUser);					// - 회원가입하기
			System.out.println("기존 회원이 아니기에 자동 회원가입을 진행합니다");
		}
		
		System.out.println("자동 로그인을 진행합니다.");
		// 로그인 처리 (=세션 등록)									// 회원일 경우 로그인 처리
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		
		System.out.println("SecurityContextHolder.getContext() : " + SecurityContextHolder.getContext());
		System.out.println("SecurityContextHolder.getContext().getAuthentication() : " + SecurityContextHolder.getContext().getAuthentication());

//		System.out.println("카카오 유저네임 : " + kakaoUser.getUsername());
//		System.out.println("저장된 카카오 유저네임 : " + "kakao_"+kakaoProfile.getId());
//		System.out.println("동일 여부 확인 : " + kakaoUser.getUsername().equals("kakao_"+kakaoProfile.getId()));
//		System.out.println("kakaoUser : " + kakaoUser);
//		System.out.println("authentication : " + authentication);
//		System.out.println("authentication.getPrincipal() : " + authentication.getPrincipal());
		
		return "redirect:/";											// "/"페이지로 이동
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm() {	
		return "user/updateForm";
	}
	
}