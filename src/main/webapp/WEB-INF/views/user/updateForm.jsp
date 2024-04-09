<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<%@ include file = "../layout/header.jsp" %>	
	
<div class="container">
	<form>
		<input type="hidden" id="id" value="${principal.user.id }">
		<div class="form-group">
			<label for="username">Username :</label>
			<input type="text" value="${principal.user.username}" class="form-control" placeholder="Enter username" id="username" readonly>	<!-- readonly : 수정불가 -->
		</div>
		
		<c:if test="${empty principal.user.oauth}">	<!--  oauth에 값이 없으면 수정가능-->
			<div class="form-group">
				<label for="password">Password :</label>
				<input type="password" class="form-control" placeholder="Enter Password" id="password">
			</div>
		</c:if>
		<div class="form-group">
			<label for="email">Email :</label>
			<input type="email" value="${principal.user.email }" class="form-control" placeholder="Enter Email" id="email" readOnly>
		</div>
		
	</form>
	<button id="btn-update" class="btn btn-primary">회원수정 완료</button>
</div>

<script src="/js/user.js"></script>	<!-- static 폴더 > js 폴더 > user.js 파일을 참조하겠다 -->
<%@ include file = "../layout/footer.jsp" %>

