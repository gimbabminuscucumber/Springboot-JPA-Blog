<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ include file = "../layout/header.jsp" %>	
	
<div class="container">	<!-- container : <div>태그로 작성한 내용을 http에서 일정부분 중앙에 정렬을 시켜준다 -->

	<div align="right">
		게시글 번호 : <span id="id"><i>${board.id } </i></span>
		작성자 : <span><i>${board.user.username } </i></span>
		작성 시간 : <span><i><fmt:formatDate pattern="yyyy-MM-dd"  value="${board.createDate }"/></i></span>
		조회수 : <span><i>${board.count } </i></span>		
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
	<br>
	
	<br>
	<!-- 댓글 -->
	<div class="card">
		<form>
			<input type="hidden" id="userId" value="${principal.user.id }"/>
			<input type="hidden" id="boardId" value="${board.id }"/>
			<div class="card-header"><span id="id">${principal.username } </span></div>
			<div class="card-body d-flex" >
				<textarea id="reply-content" class="form-control" rows="1"></textarea> &nbsp;
				<button type="button" id="btn-reply-save" class="btn btn-primary" style="width: 65px">등록</button>
			</div>
		</form>
	</div>

	<br>
	<!-- 댓글 리스트 -->
	<div class="card">
		<div class="card-header">댓글 리스트</div>
			<ul id="reply-box" class="list-group">	<!-- 내장 라이브러리가 아닌 내가 만든 임의의 별칭은 하이픈(-) 두개 쓰기 -->
				<c:if test="${empty board.replys}">
					<div><span>&nbsp;&nbsp;&nbsp; 작성된 댓글이 없습니다.</span></div>
				</c:if>
				<c:forEach var="reply" items="${board.replys }">
					<li id="reply-${reply.id }" class="list-group-item d-flex justify-content-between">
						<div>${reply.content }</div>
						<div class="d-flex">
							<div>작성자 : ${reply.user.username }  &nbsp;</div>
							<c:if test="${reply.user.id == principal.user.id }">	
								<button onclick="index.replyDelete(${board.id}, ${reply.id})" class="badge">삭제</button>
							</c:if> 
						</div>	
					</li>
				</c:forEach>
			</ul> 
	</div>
</div>

<script src="/js/board.js"></script>
<%@ include file = "../layout/footer.jsp" %>