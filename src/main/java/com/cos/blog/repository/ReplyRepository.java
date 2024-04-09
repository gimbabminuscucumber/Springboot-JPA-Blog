package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer>{	// Reply 클래스의 id가 int라서 <Reply, Integer>
	
	// 네이티브 쿼리 사용
	// - 특징 : 네이티브 쿼리를 사용하면 영속화가 필요없다
	// - @Modifying, @Query 사용
	// - ReplySaveRequestDto가 들고있는 변수들을 순서대로(userId, boardId, content) 적으면 된다
	// - 맨 마지막에 nativeQuery = true 작성
	@Modifying																		// int, Integer만 리턴할 수 있기 때문에 mSave()의 타입이 int
	@Query(value="INSERT INTO reply(userId, boardId, content) VALUES(?1, ?2, ?3)", nativeQuery = true)
	int mSave(int userId, int boardId, String content);		// JDBC는 insert, update등의 작업을 실행하면 업데이트 된 행의 개수를 리턴해줌 
}																								
