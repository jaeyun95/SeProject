<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.*" %>
<%@ page import="wedeal.bean.*" %>

<!-- 
	글쓰기 페이지
	글쓰기 양식을 모두 채워야만 글 등록 가능.
	채우지 않을 경우 BoardWriterServlet에서 session을 통해 경고 메세지를 보냄.
	글쓰기 완료되면 board.jsp로 이동.
	최종 수정: 2017/11/05
-->

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width", initial-scale="1">
	<link rel="stylesheet" href="css/bootstrap.css">
	<title>메인 화면</title>
	<script src="../js/jquery.form.min.js"></script> 
	<script src="../js/jquery.MetaData.js"></script> 
	<script src="../js/jquery.MultiFile.js"></script> 
	<script src="../js/jquery.blockUI.js"></script> 
	<script src="js/bootstrap.js"></script>
	<script src="multiupload.js"></script>
	

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
				<li><a href="board.jsp">게시판</a></li>
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
	<div class="container">
		<div class="row">
		<!-- 테이블 색 -->
			<form name=writetable method="post" action="./BoardWriteAction" enctype="multipart/form-data">
				<table class="table table-striped" style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th colspan="2" style="background-color: #eeeeee; text-align: center;">게시판 글쓰기 양식</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><input type="text" class="form-control" placeholder="글 제목" name="board_title" maxlength="50"></td>
					</tr>
					<tr>
						<td>
						<select class="form-control" id="out_cate_num" name="out_cate_num">
						<option value="">대 카테고리를 선택해 주세요</option>
						<% 
							CateDBBean cate = CateDBBean.getinstance();
							ArrayList<CateDataBean> list = cate.getList(); //대 카테고리
							for(int i = 0; i < list.size(); i++){
						%>
							<option value=""><%=list.get(i).getCate_name() %></option>
						<%
							}
						%>
						</select>
						<select class="form-control" id="cate_num" name="cate_num">
						<option>소 카테고리를 선택해 주세요</option>
						<% 
							ArrayList<CateDataBean> in_list = cate.in_getList();
							for(int i = 0; i < in_list.size(); i++){
						%>
							<option value="<%=in_list.get(i).getCate_num() %>" name="cate_num"><%=in_list.get(i).getCate_name() %></option>
						<%
							}
						%>
						</select>
						</td>
					</tr>
					<tr>
						<td><input type="text" class="form-control" placeholder="가격" name="board_price"></td>
					</tr>
					<tr>
						<td><textarea class="form-control" placeholder="글  내용" name="board_content" maxlength="2048" style="height: 350px;"></textarea></td>
					</tr>
					<tr>
						<td><label>최대 업로드 파일 수 : 5개</label></td>
					</tr>
					<tr>
						<td>
						파일: <input type="file" class="form-control" name="file1"><br>
						파일: <input type="file" class="form-control" name="file2"><br>
						파일: <input type="file" class="form-control" name="file3"><br>
						파일: <input type="file" class="form-control" name="file4"><br>
						파일: <input type="file" class="form-control" name="file5"><br>
						</td>
					</tr>
				</tbody>
			</table>
			<input type="submit" class="btn btn-primary pull-right" value="글쓰기">
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
	<div class="modal fade" id="checkModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<div id="checkType" class="modal-content panel-info">
					<div class="modal-header panel-heading">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span>
							<span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							확인 메시지
						</h4>
					</div>
					<div class="modal-body" id="checkMessage">
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</body>
</html>