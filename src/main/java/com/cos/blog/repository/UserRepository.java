package com.cos.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.User;

// JpaRepository는 User 테이블을 관리 / PK는 Integer로
// DAO 의 역할 수행 (CURD 가능)
// 자동으로 bean 등록이 된다
// @Repository 생략 가능 
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// SELECT * FROM user WHERE username = 1?;
	Optional<User> findByUsername(String username);
	
	
	// 로그인을 위한 함수 만들기 	>> 시큐리티 사용하면서 필요없어짐
	// 방법 1.
	// - JPA Naming 쿼리
	// - JPA가 가지고 있는 함수가 아님
	// - findBy뒤에 SELECT 할 쿼리를(where 뒤에 붙음) 대문자로 작성 (ex. UsernameAndPassword)
	// - SELECT * FROM user WHERE username = ? AND password = ?;
	// 	- username = ? 에는 매개변수로 받은 username 값이 들어감
	// 	- password = ? 에는 매개변수로 받은 password 값이 들어감
//	User findByUsernameAndPassword(String username, String password);
	
	// 방법 2.
	// - nativeQuery
//	@Query(value="SELECT * FROM user WHERE username = ?1 AND password = ?2", nativeQuery=true)
//	User login(String username, String password);
}
 