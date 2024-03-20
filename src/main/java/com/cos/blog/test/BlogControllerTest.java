package com.cos.blog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// JDBC를 사용 안하겠다는 어노테이션
// - application.yml 설정하면 하기 어노테이션은 불필요
// @SpringBootApplication(exclude={DataSourceAutoConfiguration.class})

// 스프링이 com.cos.blog 패키지 이하를 스캔해서 모든 파일을 메모리에 new하는 것은 아니고
// 특정 어노테이션에서 붙어있는 클래스 파일들을 new해서(IoC) 스프링 컨테이너에 관리해준다
@RestController
public class BlogControllerTest {

	// 사이트 주소 : http://localhost:8080/test/hello
	@GetMapping("/test/hello")
	public String hello() {
		return "<h1>hello spring boot</h1>";
	}
}
