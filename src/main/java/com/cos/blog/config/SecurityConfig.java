package com.cos.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.blog.config.auth.PrincipalDetailService;

// bean 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration			// bean 등록
public class SecurityConfig{ 
	
	@Autowired
	private PrincipalDetailService pricipalDetailService;
	
	// AuthenticationManager 메서드 생성
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean 					// IoC 가 된다 (제어의 역전 : 객체의 관리를 개발자에서 프레임워크로 위임)
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();				// BCryptPasswordEncoder() 는 시큐리티가 갖고있는 함수
	}

	// 1. 시큐리티가 대신 로그인하기 위해 password를 가로챘을 때
	// - 해당 password가 어떤 해쉬가 되어 회원가입이 되었는지 알게 하기 위한 메소드
	// 2. 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(pricipalDetailService).passwordEncoder(encodePWD());
	}
	
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 1. csrf 토근 비활성화 (테스트시에는 걸어두는게 좋음)
		// - CSR 할때 postman 접근해야 함  
		http.csrf(c -> c.disable());

		// 2. 인증 주소 설정 (WEB-INF/** 추가해줘야 함. 아니면 인증이 필요한 주소로 무한 리다이렉션 일어남)
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/WEB-INF/**","/", "/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**").permitAll()		// 매개변수에 적힌 주소 외에는 다 인증이 필요
				.anyRequest().authenticated());
		
		// 3. 로그인 처리 프로세스 설정
		http.formLogin(f -> f.loginPage("/auth/loginForm")
				.loginProcessingUrl("/auth/loginProc")		// 시큐리티가 해당 주소로 오는 로그인 요청을 가로채서 대신 로그인한다
				.defaultSuccessUrl("/")									// 로그인이 성공하면 이동할 주소
		);

		return http.build();
	}
}