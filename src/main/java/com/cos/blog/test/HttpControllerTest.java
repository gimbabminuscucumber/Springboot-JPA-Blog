package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 사용자가 요청했을 때 응답 해주는 컨트롤러
// - Data를 응답하면 @RestController
// - HTML를 응답하면 @Controller
@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest : ";
	
	@GetMapping("/http/lombok")
	public String lombokTest() {
		Member m = new Member(3, "asdf", "asdf4f", "email");
		System.out.println(TAG + "getter 수정 전 : " + m.getId());
		m.setId(0);
		System.out.println(TAG + "getter 수정 후 : " + m.getId());
		return "Lombok 테스트 완료";
	}
	
	@GetMapping("/http/lombok2")
		public String lombokTes2() {
			Member m1 = Member.builder().userName("test01").userPassword("098").build();
			System.out.println(TAG + "getter 수정 전 : " + m1.getUserName());
			m1.setUserName("NoTest");
			System.out.println(TAG + "getter 수정 후 : " + m1.getUserName());
			return "Lombok 테스트2 완료";
	}
	

	// 인터넷 브라우저 요청은 무조건 'get'요청 밖에 할 수 없다 (post, put, delete 요청하면 405에러)
	// - post, put, delete 요청은 postman 프로그램을 통해서 실행
	
	// 사이트 주소 : http://localhost:8080/http/get
	// - select
	// - 주소 뒤에 '?' 를 붙여 데이터 전달
	@GetMapping("/http/get")
	public String getTest(Member m) {	// id=1&userName=비둘기멋지게발차기&userPassword=1234&userEmail=asdf1234@naver.com
		return "get 요청 : " + m.getId() + ", " + m.getUserName() + ", " + m.getUserPassword() + ", " + m.getUserEmail() ;
	}
	
	// 사이트 주소 : http://localhost:8080/http/post	
	// - insert
	// - 주소가 아니라 body에 데이터를 붙여 전달
	// 방법 1. form 형태로 전달	>> postman에 www-form
//	@PostMapping("/http/post")
//	public String postTest(Member m) {
//		return "post 요청 : " + m.getId() + ", " + m.getUserName() + ", " + m.getUserPassword() + ", " + m.getUserEmail() ;
//	}
	
	// 방법 2. text 형태로 전달	>> postmas에 raw
	// - text/plain or application/json .... 쿼리 스트링의 매핑작업(=타입 파싱)은 스트링부트의 MessageConverter가 알아서 실행한다
	// 2-1. text/plain
	// - postman에 아무 문자나 작성 (데이터가 text/plain 으로 날라간다)
//	@PostMapping("/http/post")
//	public String postTest(@RequestBody String text) {
//		return "post 요청 : " + text ;
//	}
	// 2-2. application/json
	// - json 형식으로 작성 (데이터가 application/json 으로 날라간다)
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m) {
		return "post 요청 : " + m.getId() + ", " + m.getUserName() + ", " + m.getUserPassword() + ", " + m.getUserEmail() ;
	}
	
	// 사이트 주소 : http://localhost:8080/http/put
	// - update	
	@PutMapping("/http/put")
	public String putTest(@RequestBody Member m) {
		return "put 요청 : " + m.getId() + ", " + m.getUserName() + ", " + m.getUserPassword() + ", " + m.getUserEmail() ;
	}
	
	// 사이트 주소 : http://localhost:8080/http/delete
	// - delete
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
}
