<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.javaex.vo.GuestbookVo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%
    List<GuestbookVo> list = (List<GuestbookVo>)request.getAttribute("list");
%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/SiteJSP/assets/css/guestbook.css" rel="stylesheet" type="text/css">
	<title>방명록</title>
</head>
<body>

	<div id="container">
	
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		
		<div id="wrapper">
			<div id="content">
				<div id="guestbook">
					<form action="/SiteJSP/gb" method="post">
					  <input type="hidden" name="a" value="add">
						<table>
							<tr>
								<td>이름</td><td><input type="text" name="name" /></td>
								<td>비밀번호</td><td><input type="password" name="password" /></td>
							</tr>
							<tr>
								<td colspan=4><textarea name="content" id="content"></textarea></td>
							</tr>
							<tr>
								<td colspan=4 align=right><input type="submit" VALUE=" 확인 " /></td>
							</tr>
						</table>
					</form>
					<ul>
						<li>
							<br>
								<% if(list != null){
								    for(GuestbookVo vo : list){ %>
								    <table width="510" border="1">
								        <tr>
								            <td><%= vo.getNo() %></td>
								            <td><%= vo.getName() %></td>
								            <td><%= vo.getReg_date() %></td>
								            <td>
								            	<form action = "/SiteJSP/gb" method ="post">
								            		<input type="hidden" name="a" value ="deleteform">
								            		<input type="hidden" name ="no" value =<%=vo.getNo()%>>
								            		<input type="hidden" name ="dbpass" value =<%=vo.getPassword()%>>
								            		<input type="submit" name ="submit" value ="삭제">
								            	</form>
								           </td>
								        </tr>
								        <tr>
								            <td colspan=4><%= vo.getContent() %></td>
								        </tr>
								    </table>
								    <br>
								    <% } %>
								<% } %>
						</li>
					</ul>
					
				</div><!-- /guestbook -->
			</div><!-- /content -->
		</div><!-- /wrapper -->
		
	 <c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
	</div> <!-- /container -->

</body>
</html>