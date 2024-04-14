package com.cos.blog.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OrderBy;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;			// 게시글 제목
	
	@Lob									// 대용량 데이터
	@Column(columnDefinition="LONGTEXT")		// @Lob이 안되면 LONGTEXT 쓰기
	private String content; 	// 섬머노트라는 라이브러리의 <html>태그가 섞여서 디자인이 됨
	
	private int count;				// 조회수

	@CreationTimestamp
	private Timestamp createDate;
	
	@ManyToOne(fetch = FetchType.EAGER)	// Many = Board, User = One
	@JoinColumn(name="userId")						// DB 내에서 Join할 거고, userId라는 필드로 사용할 거다
	private User user;											// 작성자 / DB는 오브젝트를 저장할 수 없어서 FK를 사용하지만, 자바는 오브젝트를 저장할 수 있음

	// mappedBy : select하기 위해 만드는 것 / 연관관계의 주인이 아니니 DB에 컬럼 만들지 마세요 ("난 FK가 아니에요")
	// - "board" : Reply.java의 board 칼럼
	// fetch = FetchType : 해당 값을 언제 가져올까?
	// - EAGER : 당장 / LAZY : 필요할때
	// OneToMany 는 JoinColumn을 안 가진다 >> 테이블 구성자체가 불가능	
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)	
	@JsonIgnoreProperties({"board"})			// 자동 참조를 할 때, Board -> Reply에서 board는 제외 (무한참조를 방지하는 방법)
	@OrderBy("id desc")										// id값을 내림차순으로 정렬
	private List<Reply> replys;							// 여러 개의 댓글이 달릴거니까 List 사용
	
	
}