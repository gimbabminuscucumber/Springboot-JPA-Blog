package com.cos.blog.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

	@Id
	@GeneratedValue(strategy=IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;			// 게시글 제목
	
	@Lob									// 대용량 데이터
	private String content; 	// 게시글 내용 : 내용이 길 수 있으니 섬머노트라는 라이브러리 사용, <html>태그가 섞여서 디자인이 됨
	
	@ColumnDefault("0")		// DB에서 number로 잡을거기 때문에 따옴표(') 없이 0만 기입
	private int count;				// 조회수
	
	@ManyToOne(fetch = FetchType.EAGER)	// Many = Board, User = One
	@JoinColumn(name="userId")						// DB 내에서 Join할 거고, userId라는 필드로 사용할 거다
	private User user;											// 작성자 / DB는 오브젝트를 저장할 수 없어서 FK를 사용하지만, 자바는 오브젝트를 저장할 수 있음

	// mappedBy : 연관관계의 주인이 아니니 DB에 컬럼 만들지 마세요 ("난 FK가 아니에요")
	// - "board" : Reply.java의 board 칼럼
	// fetch = FetchType : 해당 값을 언제 가져올까?
	// - EAGER : 당장 / LAZY : 필요할때
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER)	// OneToMany 는 JoinColumn을 안 가진다 >> 테이블 구성자체가 불가능	
	private List<Reply> reply;																	// 여러 개의 댓글이 달릴거니까 List 사용
	 																		
	@CreationTimestamp
	private Timestamp createDate;
	
}