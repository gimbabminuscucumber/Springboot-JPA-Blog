package com.cos.blog.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@Service			// Bean 등록
public class PrincipalDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	// 시큐리티가 로그인 요청 시 두 개의 변수 username과 password를 가로채는 데
	// - password 부분 처리는 알아서 작업
	// - username이 DB에 있는지 확인해주면 됨 (해당 확인을 하기 메소드에서 진행)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userRepository.findByUsername(username)															// 해당 유저를 찾고
				.orElseThrow(()->{																														// 해당 유저가 없으면
					return new UsernameNotFoundException("해당 사용자(" + username + ")를 찾을 수 없습니다");	// 예외 처리
				});
		return new PrincipalDetail(principal);																								// 해당 유저가 있으면 매개변수에 담아서 PrincipalDetail 객체 생성 
	}																																									// - 시큐리티 세션에 유저의 정보가 저장됨
																																										// - 유저 정보의 데이터 타입은 implements를 했던 UserDetail 타입이다
}
