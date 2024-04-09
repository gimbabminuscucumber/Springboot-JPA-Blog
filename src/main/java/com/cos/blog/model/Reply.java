package com.cos.blog.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.cos.blog.dto.ReplySaveRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reply {

	@Id		// primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 200)
	private String content;
	
	@ManyToOne
	@JoinColumn(name="boardId")
	private Board board;			// Reply = many, board = one >> ManyToOne (여러 개의 댓글이 하나의 게시글에 달릴 수 있다)
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;				// Reply = many, user = one >> ManyToOne (여러 개의 댓글을 하나의 유저가 달 수 있다)
	
	@CreationTimestamp
	private Timestamp createDate;
	
	public void update(User user, Board board, String content) {
		setUser(user);
		setBoard(board);
		setContent(content);
	}
}
