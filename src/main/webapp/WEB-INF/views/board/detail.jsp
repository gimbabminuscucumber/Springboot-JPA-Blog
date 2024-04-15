<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ include file = "../layout/header.jsp" %>	
	
<div class="container">
		<div align="right">
			글 번호 : <span id="id"><i>${board.id}</i></span>&nbsp;/
			작성자 : <a href="#"><span><i>${board.user.username}</i></span></a>&nbsp;/
			작성시간 : <span><i><fmt:formatDate pattern="yyyy-MM-dd" value="${board.createDate}"></fmt:formatDate></i></span>&nbsp;/
			조회수 : <span><i>${board.count}</i></span>
		</div>
	<br>
	<br>

<!-- 게시글 내용 -->
	<div class="form-group">
		<b><label>Title : </label></b>
		<div>${board.title }</div>
	</div>
	<hr>
	<div class="form-group">
		<b><label>Content : </label></b>
		<div>${board.content }</div>
	</div>
	<hr>
	
	<button class="btn btn-primary" onclick="history.back()">이전</button>
	<c:if test="${board.user.id == principal.user.id}">
		<a href="/board/${board.id}/updateForm" class="btn btn-primary">수정</a>
		<button id="btn-delete" class="btn btn-primary">삭제</button>
	</c:if>
	<br>
	<br>

<!-- 댓글 -->
	<div class="card">
		<form>
			<input type="hidden" id="userId" value="${principal.user.id}" />	<!-- 댓글 작성자 확인 / id="userId"는 js로 가져감 -->
			<input type="hidden" id="boardId" value="${board.id}" />			<!-- 댓글이 달리는 게시글 확인에 사용 -->
			<div class="card-header"><span>${principal.user.username }</span></div>
			<div class="card-body d-flex">
				<textarea id="reply-content" class="form-control" rows="1"></textarea>
				<button type="button" id="btn-reply-save" class="btn btn-primary" style="width: 65px">등록</button>
			</div>
		</form>
	</div>
	<br>

<!-- 댓글 리스트 -->	
	<div class="card">
		<div class="card-header">댓글 리스트</div>
			<form>
				<input type="hidden" id="userId" value="${reply.user.id}" />	<!-- 댓글 작성자 확인 / id="userId"는 js로 가져감 -->
				<ul id="reply-box" class="list-group">
					<c:if test="${empty board.replys }">
						<div><span>&nbsp;&nbsp;&nbsp; 작성된 댓글이 없습니다.</span></div>
					</c:if>
					<c:forEach var="reply" items="${board.replys}">	<!-- Board.java의 List<Reply> replys -->
						<li id="reply-${reply.id}" class="list-group-item d-flex justify-content-between">
							<div>${reply.content}</div>
							<div class="d-flex">
								<div><span>작성자 : <i><a href="#">${reply.user.username}</a></i></span>&nbsp;</div>
								<c:if test="${reply.user.id eq principal.user.id}">
									<button onclick="index.replyDelete(${board.id}, ${reply.id})" class="badge">삭제</button>
								</c:if>
							</div>
						</li>
					</c:forEach>
				</ul>
			</form>
		</div>
	</div>

<script src="/js/board.js"></script>
<%@ include file = "../layout/footer.jsp" %>