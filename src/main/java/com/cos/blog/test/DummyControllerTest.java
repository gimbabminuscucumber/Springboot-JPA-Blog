package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

import jakarta.transaction.Transactional;

@RestController
public class DummyControllerTest { 

	@Autowired		// 의존성 주입(DI)
	private UserRepository userRepository;

	// [회원 정보 삭제]
	// http://localhost:8000/blog/dummy/user/1
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
	
		// 1. DB에 없는 데이터를 삭제 요청해도 emptyResultDataAccessException 발생 안함..
		// - deleteById 가 에러를 안 일으킴 >> 해결방법 찾아보기
		try{
	        userRepository.deleteById(id);			// DB에 회원 id를 찾아 동일한 값이 있는 경우 삭제, 없으면 예외 발생(try-catch 사용하여 예외 처리)
	    }catch (Exception e) {
	    	return "삭제에 실패했습니다. id : " + id + " 은/는 DB에 없는 회원입니다";
	    }
	    return "id : " + id + " 회원을 삭제했습니다";
	}

		// 2. 데이터 삭제 여부를 확인하기 위해 만든 궁여지책 코드... (console에서만 확인 가능)
//		if(userRepository.findById(id).isEmpty()) {
//			System.out.println("삭제에 실패했습니다. id : " + id + " 은/는 DB에 없는 회원입니다");
//		}else {
//			userRepository.deleteById(id);
//			System.out.println("id : " + id + " 회원을 삭제했습니다");
//		}
//		
//		return null;	
//	}
	
	
	// [회원 정보 수정] 
	// email, password 수정
	// http://localhost:8000/blog/dummy/user/1
	@Transactional			// 함수 종료시에 자동 commit 실행
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {	//@RequestBody : JSON 데이터를 받기 위한 어노테이션 (안쓰면 form 태그로 받아짐)
																																					// - JSON 데이터 요청 -> MessageConverter의 Jackson라이브러리가 Java Object로 변환해서 받아줌
		System.out.println("id : " + id);	
		System.out.println("password : " + requestUser.getPassword());
		System.out.println("email : " + requestUser.getEmail());
		System.out.println("requestUser.getClass() : " + requestUser.getClass());
		
		
		User user = userRepository.findById(id).orElseThrow(() ->{
			return new IllegalArgumentException("수정에 실패하였습니다");
		});
		
		user.setPassword(requestUser.getPassword());	// postman에서 넘긴 password 데이터를 requestUser로 받아 user의 setPassword에 저장
		user.setEmail(requestUser.getEmail());

		// save함수
		// - id 미전달시 : insert
		// - id 전달시 : id에 대한 데이터가 있으면 update / 없으면 insert
//		userRepository.save(user);			// save() : 'insert, update'에 사용
		return user;
	}
	
	// [모든 회원 정보 불러오기]
	// http://localhost:8000/blog/dummy/users
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();				// User의 모든 데이터 불러오기
	}
	
	// [페이징 처리]
	// 한 페이지당 2건의 데이터를 리턴받기
	// http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 2, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingUser = userRepository.findAll(pageable);		// User의 모든 데이터를 페이지 형식으로 불러오기
		
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	// [특정 회원정보 불러오기]
	// {id} 주소로 파라미터를 전달 받을 수 있음
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User datail(@PathVariable int id) {
		// DB에 없는 User id를 요청하면 null이 될텐데, null은 return이 안되니 Optional로 User 객체를 감싸서 가져올테니 null인지 아닌지 판단해서 return 해라 
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("유저" + id + " 은/는 없습니다.");
			}
		});
		// 요청하는 곳				: 웹 브라우저 (= html만 리턴할 수 있음)
		// @RestController	: html 파일이 아니라 data를 리턴해주는 controller
		// user 객체 = 자바 오브젝트
		// 스프링부트는 MessageConverter라는 애가 응답시에 자동 작동
		// 만약 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출하여 user 오브젝트를 JSON으로 변환해서 브라우저에게 던져준다
		return user;
	}
	
	// 람다식		>> 위 코드를 간단하게 변경 
//	@GetMapping("/dummy/user/{id}")
//	public User datail(@PathVariable int id) {
//		User user = userRepository.findById(id).orElseThrow(() -> {
//			return new IllegalArgumentException(id + "라는 유저는 없습니다.")
//		});
//		return user;
//	}
	
	// [회원가입]
	// http://localhost:8000/blog/dummy/join
	@PostMapping("/dummy/join")
	public String join(User user) {			// 매개변수에 Object 도 받을 수 있음
		System.out.println("id : " + user.getId());
		System.out.println("username : " + user.getUsername());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		System.out.println("role : " + user.getRole());
		System.out.println("createDate : " + user.getCreateDate());
	
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
	
}
