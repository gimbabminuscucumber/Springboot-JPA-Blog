package com.cos.blog.model;

import static jakarta.persistence.GenerationType.IDENTITY;
import java.sql.Timestamp;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// ORM = Java 또는 다른언어의 Object를 jdbc의 테이블로 매핑해주는 기술
@Entity										// User 클래스가 MySQL에 테이블이 생성된다
@DynamicInsert						// insert시, null인 필드 제외
public class User {

	@Id											// primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다
	private int id;							// 시퀀스, auto_increment
	
	@Column(nullable = false, length = 30, unique = true)		// nullable = null 허용여부 (false = 불가 / true = 허용)
	private String username;		// 아이디
	
	@Column(nullable = false, length = 100)		// 해쉬(비밀번호 암호화)하기 위해서 넉넉하게
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	
	//@ColumnDefault("'user'")		// DB에서 문자열로 쓸거기 때문에 쌍따옴표(") 안에 따오묘(')를 한번 더 써서 'user'로 입력
	// DB는 RoleType 이라는게 없다
	@Enumerated(EnumType.STRING)
	private RoleType role;				// RoleType(= Enum)에 설정한 USER, ADMIN만 입력 가능
	
	@CreationTimestamp 			// 시간이 자동 입력
	private Timestamp createDate;
}
