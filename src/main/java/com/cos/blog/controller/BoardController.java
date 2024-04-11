package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.model.Board;
import com.cos.blog.service.BoardService;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	// 컨트롤러에서 세션 찾는 방법
//		index() 메소드의 매개변수로 @AuthenticationPrincipal PrincipalDetail principal 넣으면 하기 내용 확인 가능
//		System.out.println("로그인 사용자 : " + principal);
//		System.out.println("로그인 사용자 아이디 : " + principal.getUsername());

	// 게시글 목록을 보기 위해서 index 페이지에 데이터를 가져가야 함
	// - model 사용 (model = jsp의 request와 비슷한 기능)
	// - model에 boardService.글목록()이라는 데이터를 boards에 담아서 가지고 있음
	// - 리턴할 때, viewResolver 작동해서 model의 데이터를 가져감
	@GetMapping({"", "/"})
	public String index(Model model, @PageableDefault(size = 3, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
		model.addAttribute("boards", boardService.글목록(pageable));			
		return "index";						// /WEB-INF/views/index.jsp 로 넘어감
	}
	 
	@GetMapping("/board/{id}")
	public String findById(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));

		// 게시글 열 때마다, 조회수 증가
		Board board = boardService.글상세보기(id);
		model.addAttribute("boardCount", boardService.조회수증가(board));
		
		return "board/detail";
	}
	
	// USER 권한이 필요
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";	// /WEB-INF/views/board/saveForm.jsp 로 넘어감 
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model) {	// model : 해당 데이터를 view로 가져가는 기능
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/updateForm";
	}
}