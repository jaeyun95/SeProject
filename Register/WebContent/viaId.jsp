<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="com.JavaProject.Wedeal.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>���̵� Ȯ��</title>
	<style type="text/css">
		div{
			text-align:center;
		}
	</style>
</head>
<%
   request.setCharacterEncoding("euc-kr");
   String user_name = request.getParameter("user_name");
   String Phone = request.getParameter("Phone");
   String phone1 = request.getParameter("phone1");
   String phone2 = request.getParameter("phone2");
   String user_phone = Phone+"-"+phone1+"-"+phone2;
   SearchService searchService = SearchService.getInstance();
   String user_id = searchService.searchId(user_name, user_phone);
%>
<body>
	<table width="700px" height="500px" align="center"  border="0" style="color:black; background-color: #e1f2f2; font-size:20px; ">
	<tr>
		<td>
		<table width="500px" height="100px" align="center" border=0; style="background-color:white;" >
		<tr>
			<th>���̵� Ȯ��</th>
		</tr>
		</table>
		     
    	<tr>
     		<td>
      		<table width="450px" align="center" border="0" style="font-size:19px">     						
       			<tr>
        		<td>
		        <%if(user_id!= null){ %>
				<tr>
				    <td><div><%=user_name %>���� ���̵��</div></td>
				</tr>
				<tr>
					<td><h1><div><%=user_id %></div></h1><div>�Դϴ�.</div></td>
				</tr>
		        </td>
				</tr>
				<tr>
					<td>
					<table width="700px" align="center"  border="0" style="color:black;; margin-top:5%; font-size:20px; ">
					<tr>
        				<td>
        				<div>
        					<input type="button" value="�α����ϱ�" class="btn btn-primary" onclick="location.href='login.jsp'">
        					<input type="button" value="��й�ȣ ã��" class="btn btn-primary" onclick="location.href='SearchPW.jsp'">        							
        				</div>
        				</td>
         			</tr>
      				</table>
      				<%} else{%>
      				<tr>
						<td><%=user_name %>��!</td>
					</tr>
					<tr>
						<td><h1>���������� �����ϴ�.</h1></td>
					</tr>
					</table>
					</td> 
				</tr>       
				<tr> 
					<td>
					<table width="150px"  align="center" border="0" style="margin-top:1%">
					<tr>
						<td><input type="button" value="ȸ�������ϱ�" class="btn btn-primary" onclick="location.href='join.jsp'"></td>
						<td><input type="button" value="ó������" class="btn btn-primary" onclick="location.href='login.jsp'"></td>
					</tr>
					</table>   
					<%} %>
          			</table>
     			</td>
			</tr> 
		</table>
</body>
</html>