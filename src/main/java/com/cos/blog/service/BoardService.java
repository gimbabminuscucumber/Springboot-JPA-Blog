package com.cos.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional								
	public void 글쓰기(Board board, User user) {		// http에서 title, content 만 받으면 됨
		System.out.println("BoardService");
		board.setCount(0);						//  조회수(count)는 0부터 시작되게 설정
		board.setUser(user);
		boardRepository.save(board);
	}
	
	@Transactional(readOnly=true)		// select만 하기 때문에 readOnly=true
	public Page<Board> 글목록(Pageable pageable){
		return boardRepository.findAll(pageable);
	}

	@Transactional(readOnly=true)		// select만 하기 때문에 readOnly=true
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패 : 아이디(" + id + ")를 찾을 수 없음");
				});
	} 

	@Transactional
	public void 글삭제(int id) {
		boardRepository.deleteById(id);
	}

	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패 : 아이디(" + id + ")를 찾을 수 없음");
				});	// 영속화 완료
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수 종료시(=Service가 종료될 때) 트랜잭션이 종료되며 이때, 더티체킹 작되어 자동 업데이트 됨(= DB에 Flush)
	}
	
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
		
		User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(()->{
			return new IllegalArgumentException("댓글 작성 실패 : 유저 id를 찾을 수 없습니다.");
		});	// 영속화 완료

		Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(()->{
			return new IllegalArgumentException("댓글 작성 실패 : 게시글 id를 찾을 수 없습니다.");
		});	// 영속화 완료
		
		// 방법 1.
		// - board.js의 replySave에서 보낸 데이터를 받기
//		Reply reply = Reply.builder()
//				.user(user)
//				.board(board)
//				.content(replySaveRequestDto.getContent())
//				.build();
		
		// 방법 2. 
		// - model.Reply에서 update() 메소드를 만들어서 데이터 받기
		Reply reply = new Reply();
		reply.update(user, board, replySaveRequestDto.getContent());
		
		replyRepository.save(reply);
	}
}