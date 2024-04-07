<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<%@ include file = "../layout/header.jsp" %>	
	
<div class="container">

	<div>
		게시글 번호 : <span id="id"><i>${board.id } </i></span>
		작성자 : <span><i>${board.user.username } </i></span>
	</div>
	<br>
	
	<div class="form-group">
		<b><label for="title">Title :</label></b>
		<div>${board.title }</div>
	</div>
	<hr>
	<div class="form-group">
		<b><label for="content">Content :</label></b>
		<div>${board.content }</div>
	</div>
	<hr>

	<button class="btn btn-primary" onclick="history.back()">이전</button>
	<c:if test="${board.user.id == principal.user.id }">	<!-- 삭제버튼이 게시글의 유저 id와 세션에 저장된 회원의 유저 id가 같을때만 보이게 -->
		<a href="/board/${board.id }/updateForm" class="btn btn-primary">수정</a>
		<button id="btn-delete" class="btn btn-primary" >삭제</button>
	</c:if> 
	
<%-- 	<div><span>게시글 작성자 번호 : ${board.user.id}</span></div>
	<div><span>로그인 유저 번호 : ${principal.user.id}</span></div> --%>
	
</div>

<script src="/js/board.js"></script>
<%@ include file = "../layout/footer.jsp" %>

