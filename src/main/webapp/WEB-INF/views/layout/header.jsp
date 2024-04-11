<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal" var="principal"/>
</sec:authorize>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>비둘기멋지게발차기의 Blog</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
	<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
	<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
	<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
</head>
<body>

	<nav class="navbar navbar-expand-md bg-dark navbar-dark">
		<a class="navbar-brand" href="/">MyBlog</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="collapsibleNavbar">
			
			<c:choose>
				<c:when test="${empty principal}">	<!-- 세션이 없으면 (= 로그인 안돼 있으면) -->
					<ul class="navbar-nav">
						<li class="nav-item"><a class="nav-link" href="/auth/loginForm">로그인</a></li>
						<li class="nav-item"><a class="nav-link" href="/auth/joinForm">회원가입</a></li>
					</ul>
				</c:when>
				<c:otherwise>									<!-- 세션이 있으면 (= 로그인 돼 있으면) -->
					<ul class="navbar-nav">
						<li class="nav-item"><a class="nav-link" href="/board/saveForm">글쓰기</a></li>
						<!-- 드롭다운 -->
						<li class="dropdown"><a class="nav-link" href="/logout" data-toggle="dropdown">회원정보▽</a>
							<div class="dropdown-menu">
								<a class="dropdown-item" href="#">내 블로그</a>
								<a class="dropdown-item" href="/user/updateForm">정보수정</a>
								<a class="dropdown-item" href="/logout">로그아웃</a>
							</div>
						</li>
						
						
					</ul>
				</c:otherwise>
			</c:choose>
				
		</div>  
	</nav>
	<br />
