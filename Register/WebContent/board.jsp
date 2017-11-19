<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="board.*" %>
<%@ page import="java.util.*" %>
<%@ page import="wedeal.bean.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 
	게시판 페이지
	게시판 페이지는 회원 session없이도 접속 가능함.
	회원 session이 없다면 '글쓰기'버튼 비활성.
	글 목록은 번호,제목,작성자,작성일을 보여줌.
	boardDAO의 getList메소드를 사용하여 글 목록을 받아오고, pageNumber를 통해 10개씩 글을 잘라 목록을 만듬.
	글이 10개 미만일 경우 아무 버튼 없음. 10개 이상일 경우 '다음','이전'버튼 활성.
	게시글의 제목을 클릭할 경우 write.jsp로 넘어감.
	최종 수정: 2017/11/05
-->

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>메인 화면</title>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<meta name="viewport" content="width=device-width", initial-scale="1">
	<link rel="stylesheet" href="css/bootstrap.css">
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
	<style type="text/css">
	a, a:hover{
		color: #000000;
		text-decoration: none;
	}
	</style>
	<%
		int pageNumber = 1; //기본페이지 초기값
		if(request.getParameter("pageNumber") != null){
			pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		}
	%>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
			aria-expanded="false">
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.jsp">중고 장터</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="active"><a href="index.jsp">메인</a></li>
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
		</div>
	</nav>
	
	<div id="catelist">
	<ul>
	<li><a href="board.jsp">게시판</a></li>
	<%
	//수정할것임
		CateDBBean cate = CateDBBean.getinstance();
		ArrayList<CateDataBean> out_cate = cate.getList();
		ArrayList<CateDataBean> in_cate = cate.in_getList();
		for(int i = 0; i < out_cate.size(); i++){
	%>
		<li><a href="board.jsp?cate_num=<%=out_cate.get(i).getCate_num()%>"><%= out_cate.get(i).getCate_name()%></a></li>
		<ul>
	<%
		for(int j = 0; j < in_cate.size(); j++){
			if(in_cate.get(j).getCate_parent() == out_cate.get(i).getCate_num()){
	%>
		<li><a href="board.jsp?cate_num=<%=in_cate.get(j).getCate_num()%>"><%=in_cate.get(j).getCate_name()%></a></li>
	<%
			}}
	%>
		</ul>
	<%	
		}
	%>
		</ul>
	</div>
	<div class="container">
		<div class="row">
		<!-- 테이블 색 -->
			<table class="table table-striped" border: 1px solid #dddddd" width="1000" height="600">
				<thead>
					<tr>
						<th colspan="5" style="background-color: #eeeeee; text-align: center;" height="30">게시판</th>
					</tr>
				</thead>
				<tbody>
					<tr>
				<%
					BoardDBBean board = BoardDBBean.getinstance();
					ArrayList<BoardDataBean> list = board.getList(pageNumber);
					int length = 0;
					
					//카테고리를 선택했을 때
					if(request.getParameter("cate_num") != null){
						int cate_num = Integer.parseInt(request.getParameter("cate_num"));
						length = board.allCount(cate_num);
						
							//만약에 클릭된 in_cate가 있다면 그것만 보여주고 아니라면 전체보여주기
						}
					
					//카테고리를 선택하지 않았을 때
					else{
						length = board.allCount(0);
					}
						
					if(list == null){
				%>
						<td align="center">등록된 게시글이 없습니다.</td>
				<%
					}
					else{
						//8에 나누어 떨어지면 페이지를 하나 더 만들 필요가 없음
						if(length%8 == 0)
							length = length/8;
						//8보다 크고 나머지가 있다면 하나의 페이지를 더 만들어 줘야함
						else if((length%8) != 0)
							length = length/8 + 1;
						for(int i = 0; i < list.size(); i++){
							String image = list.get(i).getBoard_image();
							String[] images = image.split("/");
				%>
						<td align="center"><a href="view.jsp?board_num=<%=list.get(i).getBoard_num() %>&user_id=${user_id}"><%=list.get(i).getBoard_title().replaceAll(" ","&nbsp;").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\n","<br>")%></a><%="<br>"%>
						<label>작성자:<%=list.get(i).getUser_id() %></label><%="<br>"%>
						<a href="view.jsp?board_num=<%=list.get(i).getBoard_num()%>&user_id=${user_id}"><img src="<%= list.get(i).getBoard_path() %>\<%= images[0] %>" height= 200px width=200px></a><%="<br>"%>
						<%=list.get(i).getBoard_date().substring(0,11) + list.get(i).getBoard_date().substring(11,13)+"시" + list.get(i).getBoard_date().substring(14,16)+"분"%></td>
				<% 
						if((i+1)%4==0 && i>0) {
				%>
						</tr>
						<tr>
				<% 
							}
						}
					}
				%>
					</tr>
				</tbody>
			</table>
			<% 
				for(int j = 0; j < length; j++){
			%>
				<a href="board.jsp?pageNumber=<%=j+1%>" class="btn btn-success btn-arraw-left"><%=j+1%></a>
			<% 
				}
			%>
				<c:if test="${user_id ne null}">
				<a href="write.jsp" class="btn btn-primary pull-right">글쓰기</a>
				</c:if>
		</div>
	</div>
</body>
</html>