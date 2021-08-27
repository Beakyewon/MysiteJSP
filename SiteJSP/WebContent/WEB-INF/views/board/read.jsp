<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.javaex.vo.BoardVo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%
    BoardVo vo = (BoardVo)request.getAttribute("boardVo");

%>

<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	function down(filename){
		 document.downFrm.filename.value = filename;
		 document.downFrm.submit();
	}
</script>
	<title>mysite</title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="/SiteJSP/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">

		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<div id="content">
			<div id="board" class="board-form">
				<table class="tbl-ex">
					<tr>
						<th colspan="2">글보기</th>
					</tr>
					<tr>
						<td class="label">제목</td>
						<td><%if(vo.getTitle() == null) {%>
								제목없음
							<%} else { %>
								<%= vo.getTitle() %>
							<%} %></td>

					</tr>
					<tr>
						<td class="label">첨부파일</td>
						<td>
							<% if( vo.getFilename() !=null && !vo.getFilename().equals("")) {%>
						  		<a href="javascript:down('<%=vo.getFilename()%>')"><%=vo.getFilename() %></a> 
						  	<%} else{%><%}%>
						  	
						  	<% if( vo.getFilename2() !=null && !vo.getFilename2().equals("")) {%>
						  		<a href="javascript:down('<%=vo.getFilename2()%>')"><%=vo.getFilename2() %></a> 
						  	<%} else{%><%}%>
						  	
						  	<% if( (vo.getFilename() ==null || vo.getFilename().equals(""))&&(vo.getFilename2() ==null || vo.getFilename2().equals(""))) {%>
						  		<p style ="color:gray">등록된 파일이 없습니다.</p>
						  	<%}%>
						  	
					<tr>
						<td class="label">내용</td>
						<td>
							<div class="view-content">
								<%if(vo.getContent()== null) {%>
								내용없음
							<%} else { %>
								<%= vo.getContent() %>
							<%} %>
							</div>
						</td>
					</tr>
				</table>
				<c:choose>
				<c:when test="${authUser.name == boardVo.userName}">
				<div class="bottom">
					<a href="/SiteJSP/board">글목록</a>
					<a href="/SiteJSP/board?a=modifyform&no=<%=vo.getId() %>">글수정</a>
				</div>
				</c:when>
				<c:otherwise>
				<div class="bottom">
					<a href="/SiteJSP/board">글목록</a>
				</div>
				</c:otherwise>
				</c:choose>
				<form name="downFrm" action="/SiteJSP/boardpost?a=download" method="post">
					<input type="hidden" name="filename">
				</form>
			</div>
		</div>

		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div>
</body>
</html>