<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="user.*" %>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="../css/bootstrap.css">
	<title>ȸ�� ��й�ȣ ����</title>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="../js/bootstrap.js"></script>
	<script type="text/javascript">
		function logout(){
			var user_id = $('#user_id').val();
			$.ajax({
				type: 'POST',
				url: './../UserLogoutServlet',
			})
		}
		function passwordCheckFunction(){
			var user_pw = $('#user_pw').val();
			var check_passwd = $('#check_passwd').val();
			if(user_pw != check_passwd){
				$('#passwordCheckMessage').html('��й�ȣ�� ���� ��ġ���� �ʽ��ϴ�.');
			}else{
				$('#passwordCheckMessage').html('');
			}
		}
	</script>
<jsp:useBean id="user" class="user.userDAO" scope="session" />
</head>

<body>
	<%
		String session_id = null;
		//���� ����
		if(session.getAttribute("user_id")!=null){
			session_id = (String)session.getAttribute("user_id");
		}
		
		userDTO userinfo = user.getUser(session_id);
		
	%>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.jsp">WeDEAL</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="active"><a href="index.jsp">����</a></li>
				<li><a href="board.jsp">�Խ���</a></li>
			</ul>
			<%
				if(session_id == null){
				//-------------------------------------------------------�α����� �Ǿ����� ���� ���
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">�����ϱ�<span class="caret"></span></a>
					 <ul class="dropdown-menu">
					 	<li><a href="login.jsp">�α���</a></li>
					 	<li><a href="join.jsp">ȸ������</a></li>
					 </ul>
				</li>
			</ul>
			<% 
				} else{
				//-------------------------------------------------------�α����� �Ǿ��ִ� ���
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">����<span class="caret"></span></a>
					 <ul class="dropdown-menu">
					 	<li><a href="" onclick="logout();">�α׾ƿ�</a></li>
					 	<li><a href="userinfo.jsp" >�� ����</a></li>
					 </ul>
				</li>
			</ul>
			<%
				} 
			%>
		</div>
	</nav>
	
	<!-- ȸ�� �޴� -->
	<div id="menu" style="display:inline-block; border-right:1px solid; float:left; height:400px; width:15%; padding:10px;">
		<ul style="list-style:none;">
			<li><a href="#">�� �Խñ�</a></li>
			<li><a href="#">�� ���</a></li>
			<li><a href="#">�� ��</a></li>
		</ul>
	</div>
	<div>
		<div style="display:inline-block; border:1px; height:200px; width:500px; padding-left:50px; padding-right:50px;">
			<h3>��й�ȣ ����</h3>
			<br>
			<div>
				<form name="modify" method="post" onsubmit="return checkpass();" action="./../UserPassWd">
				��й�ȣ : <input type="password" name="user_pw" size="15" />
				<br>
				��й�ȣ Ȯ�� : <input type="password" name="check_passwd" size="15" />
				<hr>
				<input type="submit" name="Submit" value="����" >
				</form>
			</div>
		</div>
	</div>
	
</body>
</html>