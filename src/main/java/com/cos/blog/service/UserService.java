package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Transactional									// 하기의 트랜잭션(들)이 하나로 묶이게 됨
	public void 회원가입(User user) {
		userRepository.save(user);			// save() 에러시, GlobalExceptionHandler가 받아서 예외처리하기 때문에 try~catch 필요없음
		
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
	*/
	}

	@Transactional(readOnly = true) 	// Select할 때 트랜잭션 시작, 서비스 종료시에 트랜잭션 종료 (정합성 유지가능)
	public User 로그인(User user) {			// userRepository.findByUsernameAndPassword가 User를 리턴하기 때문에 로그인() 리턴도 User
		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
	}
}
