<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="wedeal.bean.*" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="menutag" %>
<!-- 
	main페이지
	로그인이 되어있지 않은 경우 접속하기를 통해 로그인, 회원가입이 가능. 로그인은 login.jsp로, 회원가입은 join.jsp로 이동.
	로그인이 되어있는 경우 마이 페이지를 통해 로그아웃 가능. UserLogoutServlet을 통해 session이 invalidate()됨.
	최종 수정: 2017/11/05
 -->
 
<!DOCTYPE html >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/bootstrap.css">
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<title>메인 화면</title>
	<script type="text/javascript">
		function logout(){
			var user_id = $('#user_id').val();
			$.ajax({
				type: 'POST',
				url: './LogoutAction',
			})
		}
	</script>
</head>

<body>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
			aria-expanded="false">
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.jsp">Wedeal</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="index.jsp"><span class="glyphicon glyphicon-home" aria-hidden="true"></span> 메인</a></li>
			</ul>
			<c:if test="${user_id eq null}">
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
					 aria-expanded="false">접속하기<span class="caret"></span></a>
					 <ul class="dropdown-menu">
					 	<li><a href="login.jsp">로그인</a></li>
					 	<li><a href="join.jsp">회원가입</a></li>
					 </ul>
				</li>
			</ul>
			</c:if>
			<c:if test="${user_id ne null}">
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
					 aria-expanded="false">마이 페이지<span class="caret"></span></a>
					 <ul class="dropdown-menu">
					 	<li><a href="" onclick="logout();">로그아웃</a></li>
					 </ul>
				</li>
			</ul>
			</c:if>
			<!-- 은진이 검색부분 연결!!! -->
			<form class="navbar-form navbar-right" role="search">
        	<div class="form-group">
          	<input type="text" class="form-control" placeholder="Search">
        	</div>
        	<button type="submit" class="btn btn-default">Search</button>
      		</form>
		</div>
	</nav>

	<!-- 메뉴 생성 부분 -->
	<menutag:menu/>	
</body>
</html>