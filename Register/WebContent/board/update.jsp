<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="board.boardDAO" %>
<%@ page import="board.boardDTO" %>
<!-- 
	수정 페이지
	view.jsp에서 수정 버튼을 누르면 넘어오는 페이지.
	
	최종 수정: 2017/11/05
-->

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width", initial-scale="1">
	<link rel="stylesheet" href="css/bootstrap.css">
	<title>메인 화면</title>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script type="text/javascript">
		function logout(){
			var user_id = $('#user_id').val();
			$.ajax({
				type: 'POST',
				url: './UserLogoutServlet',
			})
		}
	</script>
</head>
<body>
	<%
		String session_id=null;
	
		if(session.getAttribute("user_id") !=null){
			session_id=(String)session.getAttribute("user_id");
		}
		if(session_id == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인이 필요합니다.')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		}
		int board_num = 0;
		
		if(request.getParameter("board_num") != null){
			board_num = Integer.parseInt(request.getParameter("board_num"));
		}
		
		if(board_num == 0){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'board.jsp'");
			script.println("</script>");
		}
		
		boardDTO boarddto = new boardDAO().getBoard(board_num);
		
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
				<li><a href="index.jsp">메인</a></li>
				<li class="active"><a href="board.jsp">게시판</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
					 aria-expanded="false">마이 페이지<span class="caret"></span></a>
					 <ul class="dropdown-menu">
					 	<li><a href="" onclick="logout();">로그아웃</a></li>
					 </ul>
				</li>
			</ul>
		</div>
	</nav>
	<div class="container">
		<div class="row">
		<!-- 테이블 색 -->
			<form method="post" action="./boardUpdate">
				<table class="table table-striped" style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th colspan="2" style="background-color: #eeeeee; text-align: center;">게시판 수정 양식</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><input type="text" class="form-control" placeholder="글 제목" name="board_title" maxlength="50" value="<%= boarddto.getBoard_title()%>"></td>
					</tr>
					<tr>
						<td><textarea class="form-control" placeholder="글  내용" name="board_content" maxlength="2048" style="height: 350px;"><%= boarddto.getBoard_content()%></textarea></td>
					</tr>
					<tr>
						<td><label>사진 첨부하기</label><input type="file" name="board_image"></td>
					</tr>
				</tbody>
			</table>
			<input type="submit" class="btn btn-primary pull-right" value="글수정">
			</form>
		</div>
	</div>
	<%
		String messageContent = null;
		if(session.getAttribute("messageContent") !=null) {
			messageContent = (String) session.getAttribute("messageContent");
		}
		String messageType = null;
		if(session.getAttribute("messageType") !=null) {
			messageType = (String) session.getAttribute("messageType");
		}

		if(messageContent != null){
	%>
	
	<div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<div class="modal-content <% if(messageType.equals("오류 메시지")) out.print("panel-warning"); else out.print("panel-success");%>">
					<div class="modal-header-panel-heading">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span>
							<span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							<%= messageType %>
						</h4>
					</div>
					<div class="modal-body">
						<%= messageContent %>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$('#messageModal').modal("show");
	</script>
	<%
		session.removeAttribute("messageContent");
		session.removeAttribute("messageType");
		}
	%>
</body>
</html>