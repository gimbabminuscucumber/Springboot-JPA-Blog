<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<%@ include file = "../layout/header.jsp" %>	
	
<div class="container">
	<form action="/action_page.php">
		<div class="form-group">
			<label for="username">Username :</label>
				<input type="text" class="form-control" placeholder="Enter username" id="username">
		</div>
		
		<div class="form-group">
			<label for="password">Password:</label>
				<input type="password" class="form-control" placeholder="Enter Password" id="password">
		</div>

		<div class="form-group">
			<label for="email">Email :</label>
				<input type="email" class="form-control" placeholder="Enter Email" id="email">
		</div>
	</form>
	<button id="btn-save" class="btn btn-primary">회원가입 완료</button>
</div>

<script src="/blog/js/user.js"></script>	<!-- static 폴더 > js 폴더 > user.js 파일을 참조하겠다 -->

<%@ include file = "../layout/footer.jsp" %>

