<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<%@ include file = "../layout/header.jsp" %>	
	
<div class="container">
	<form action="/auth/loginProc" method="post">
		<div class="form-group">
			<label for="username">Username :</label>
			<input type="text" name="username" class="form-control" placeholder="Enter username" id="username">
		</div>

		<div class="form-group">
			<label for="password">Password :</label>
			<input type="password" name="password" class="form-control" placeholder="Enter Password" id="password">
		</div>

	<button id="btn-login" class="btn btn-primary">로그인</button>
	<a href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=7481d3f8938ab995972608ade8c364bd&redirect_uri=http://localhost:8000/auth/kakao/callback"><img height="38px" src="/image/kakao_login_button.png"></a>
	</form>
</div>

<%@ include file = "../layout/footer.jsp" %>