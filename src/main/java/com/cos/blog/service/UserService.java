package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

// 스프링이 컴포넌트 스캔을 통해 Bean에 등록을 해줌 (IoC를 해줌)
// service가 필요한 이유
// - 트랜잭션 관리
// - 서비스 의미 때문

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired								
	private BCryptPasswordEncoder encoder;								// BCryptPasswordEncoder가 DI로 주입
	
	@Transactional(readOnly=true)
	public User 회원찾기(String username) {
		User user = userRepository.findByUsername(username).orElseGet(()->{	// 회원을 못 찾으면
			return new User();																							// - 빈 객체를 반환
		});																				
		return user;																											// 회원을 찾으면 해당 객체를 반환
	}
	
	@Transactional									// 하기의 트랜잭션(들)이 하나로 묶이게 됨
	public void 회원가입(User user) {
		String rawPassword = user.getPassword();						// 입력받은 비밀번호 원문
		String encPassword = encoder.encode(rawPassword);	// 해쉬 시킨 비밀번호
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);		// http에서 user 객체로 받아온 데이터는 username, password, email 뿐이라서 role은 직접 추가
		userRepository.save(user);			// save() 에러시, GlobalExceptionHandler가 받아서 예외처리하기 때문에 try~catch 필요없음
//		try {
//			userRepository.save(user);
//			return 1;
//		} catch (Exception e) {
//			return -1;
//		}
	}

	@Transactional
	public void 회원수정(User user) {
		// 수정시에는 영속성 컨텍스트에 User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정하기
		// select를 해서 User 오브젝트를 DB로부터 가져오기 (영속화를 위해서)
		// - 영속화된 오브젝트를 변경하면 자동으로 DB에 update를 날림
		User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
			return new IllegalArgumentException("회원 찾기 실패");
		});
		
		// Validate 체크
		// - oauth에 값이 없으면 필드 수정가능
		if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);
			persistance.setEmail(user.getEmail());
		}

		// 회원 수정 함수 종료 = 서비스 종료 = 트랜젝션 종료 = 자동으로 commit 진행 
		// = 영속화된 persistance 객체의 변화가 감지되면 더티체킹이 되어 update문을 날려줌
	}
		
	/*
	public int 회원가입(User user){			// 리턴값이 '정수'라서 메소드 타입은 int
		try {
			userRepository.save(user);		// 트랜잭션 (= DB의 상태를 변화시키기 위해 수행하는 작업 (ex.Select, Insert, Delete, Update))
			return 1;										// save()가 정상이면  1을 리턴
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("UserService : 회원가입() : " + e.getMessage());
		}
		return -1;										// save()가 비정상적이면 -1을 리턴
	}
	*/

	// 시큐리티 사용하면서 필요없어짐
//	@Transactional(readOnly = true) 	// Select할 때 트랜잭션 시작, 서비스 종료시에 트랜잭션 종료 (정합성 유지가능)
//	public User 로그인(User user) {			// userRepository.findByUsernameAndPassword가 User를 리턴하기 때문에 로그인() 리턴도 User
//		System.out.println("UserService, user : " + user);
//		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//	}
}
