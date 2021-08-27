<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.javaex.vo.BoardVo"%>
<%@page import="com.javaex.vo.UserVo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>


<%
	UserVo authUser = (UserVo)session.getAttribute("authUser");
	List<BoardVo> list = (List<BoardVo>)request.getAttribute("list") ;
	
	int nowPage=1;
	int numPerPage=10;
	int totalRecord = (int) request.getAttribute("totalCount");
	
	int totalPage = (int)Math.ceil((double)totalRecord / numPerPage);
%>

<!DOCTYPE html>
<html>
<head>
	<title>Mysite</title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="/SiteJSP/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<div id="content">
			<div id="board">
				<form id="search_form" action="/SiteJSP/board" method="post">
					<input type = "hidden" name = "a" value="list">
					<select name="keyField" size="1" >
	    				<option value="name"> 	 글쓴이</option>
	    				<option value="title"> 	 제 목</option>
	    				<option value="content"> 내 용</option>
	    				<option value="regdate"> 작성일</option>
   					</select>
					<input type="text" id="kwd" name="kwd" value="" placeholder="작성일은 YYYYMMDD 형태로 검색하세요">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
						
					</tr>
					<% if(list != null){
						for(BoardVo vo : list){ %>
					<tr>
						<td><%= vo.getNum() %></td>
						<td><a href="/SiteJSP/board?a=read&no=<%= vo.getId()%>&hit=<%= vo.getHit()+1%>">
							<%if(vo.getTitle() == null) {%>
								제목없음
							<%} else { %>
								<%= vo.getTitle() %>
							<%} %></a>
						</td>	
						<td><%= vo.getUserName() %></td>
						<td><%= vo.getHit() %></td>
						<td><%= vo.getReg_date() %></td>
						<td>
							<% if(authUser != null && vo.getUser_id () == authUser.getId()) { %>
								<input type="image" style ="width: 15px" src="/SiteJSP/assets/images/recycle.png"onClick="if(confirm('정말로 삭제하시겠습니까?')){location.href='/SiteJSP/board?a=delete&no=<%=vo.getId() %>';}">
							<% } else {%>
							<% } %>
						</td>	
					</tr>
						<% } %>
					<% }else{ %>
						<p align ="center"> 등록된 게시물이 없습니다 </p>
					<%} %>
				</table>
				<div class ="pager">
					<ul>
					<%
						if(totalPage != 0){
							for(int i=1; i<totalPage+1; i++){%>
								<li><a href ="/SiteJSP/board?a=list&nowPage=<%= i%>"> <%=i %> </a></li>
							<% }
						}
					%>
					</ul>
				</div>		
				<c:choose>
					<c:when test="${authUser == null}">
					</c:when>
					<c:otherwise>
						<div class="bottom">
							<a href="/SiteJSP/board?a=writeform" id="new-book">글쓰기</a>
						</div>	
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
	</div>
</body>
</html>