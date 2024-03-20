package com.cos.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Getter							// getter 어노테이션
//@Setter							// setter 어노테이션
@Data									// getter, setter 어노테이션
//@AllArgsConstructor		// 모든 필드 추가한 생성자 어노테이션
@NoArgsConstructor		// 빈 생성자 어노테이션
public class Member {
	
	// 객체지향의 특성을 살려 외부에서 접근 불가능하게 만들기 위해 'private' 사용
	private int id;
	private String userName;
	private String userPassword;
	private String userEmail;
	
	@Builder							// 매개변수를 다르게 만든 생성자를 별도로 만들 필요 없음
	public Member(int id, String userName, String userPassword, String userEmail) {		//@AllArgsConstructor 는 주석처리
		super();
		this.id = id;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userEmail = userEmail;
	}
}
