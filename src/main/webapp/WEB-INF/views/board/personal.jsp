<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="..layout/header.jsp" %>	

<div class="container">

	<form>
		<input type="hidden" id="userId" value="${user.id}" />
		<div class="card m-2">
			<div class="card-header">
				<i class="mtrl-select">나만의 게시글</i>
			</div>
		</div>
	
		<!-- 게시글 -->
		<c:forEach  var="personal" items="${personal.content}">
			<div class="card m-2" >
				<div class="card-body">
					<div class="d-flex justify-content-between">
						<c:if test="${empty board }">
							<span>작성한 게시글이 없습니다.</span>
						</c:if>

						<h4 class="card-title">${board.title}</h4>
						<div>작성자 : <a href="#">${principal.username }</a></div>
					</div>
					<div>
						<a href="/board/${board.id }" class="btn btn-primary">상세 보기</a>
					</div>
				</div>
			</div>
		</c:forEach>
		
		<!-- 페이지 버튼 -->
		<br>
		<ul class="pagination justify-content-center">
		
			<c:choose>
				<c:when test="${boards.first }">		<!-- boards에서 페이지가 fisrt이면 'Previous' 버튼 클릭 불가-->
					<li class="page-item disabled"><a class="page-link" href="?page=${boards.number-1 }">Previous</a></li>
				</c:when>
				<c:otherwise>							<!-- boards에서 페이지가 fisrt아니면 'Previous' 버튼 클릭 가능 -->
					<li class="page-item"><a class="page-link" href="?page=${boards.number-1 }">Previous</a></li>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${boards.last }">
					<li class="page-item disabled"><a class="page-link" href="?page=${boards.number+1 }">Next</a></li>
				</c:when>
				<c:otherwise>
					<li class="page-item"><a class="page-link" href="?page=${boards.number+1 }">Next</a></li>
				</c:otherwise>
			</c:choose>
		
		</ul>
	</form>
</div>

<%@ include file = "..layout/footer.jsp" %>