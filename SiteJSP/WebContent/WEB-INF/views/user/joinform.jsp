<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%
	String email = (String)request.getAttribute("email");
%>
<!DOCTYPE html>
<html>
<script type="text/javascript">

function checkValue(){
	var form = document.joinForm;
	
	if(!form.email.value){
		alert("이메일을 입력하세요.");
		return false;
	}
	if(form.idCheck.value.equals("idUncheck")){
		alert("이메일 중복체크를 해주세요.");
		return false;
	}
	if(!form.name.value){
		alert("이름을 입력하세요.");
		return false;
	}
	if(!form.password.value){
		alert("비밀번호를 입력하세요.");
		return false;
	}
	if(form.password.value != form.passwordCheck.value ){
        alert("비밀번호를 동일하게 입력하세요.");
        return false;
    }    
	if(form.agreeProv.value != "y"){
		alert("약관을 동의해주세요.");
		return false;
	}
}

function idcheck(){
	
	let idcheckFrm = document.joinForm;
	let email = idcheckFrm.email.value;
	
	if(email.length == 0 || email == ""){
		alert("이메일을 입력하세요.");
	}else{
		idcheckFrm.method = "post";
		idcheckFrm.action = "/SiteJSP/join?a=idcheck";
		idcheckFrm.submit();
	}
}

</script>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/SiteJSP/assets/css/user.css" rel="stylesheet" type="text/css">
	<title>회원가입</title>
</head>
<body>

	<div id="container">
		
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
			
		<div id="wrapper">
			<div id="content">
				<div id="user">
	
					<form id="join-form" name="joinForm" method="post" action="/SiteJSP/join" onsubmit ="return checkValue()">
						<input type='hidden' name="a" value="join">
	<%--
						<label class="block-label" for="email">이메일</label>			
						<input id="email" name="email" type="text" value="">
						<input type="button" onClick="idcheck()" value=" id중복체크 ">
	--%>
	
						<label class="block-label" for="email">이메일</label>	
						<input id="email" type="text" name="email"  value="${email}"> 
						<input type="button" onClick="idcheck()" value=" ID 중복체크 ">
						
						<c:choose>
							<c:when test="${param.result eq 'fail'}">
								<p style ="color:red"> 중복된 이메일입니다. </p>
								<input type ="hidden" name ="idCheck" value="idUncheck">
							</c:when>
							<c:when test ="${param.a eq 'idcheck'}">
								<p style ="color:blue"> 사용가능한 이메일입니다. </p>
								<input type ="hidden" name ="idCheck" value="idCheck">
							</c:when>
							<c:otherwise>
								<input type ="hidden" name ="idCheck" value="idUncheck">
							</c:otherwise>
						</c:choose>
						
						<label class="block-label" for="name">이름</label>
						<input id="name" name="name" type="text" value="">
						
						<label class="block-label">비밀번호</label>
						<input name="password" type="password" value="">
						
						<label class="block-label">비밀번호 확인</label>
						<input name="passwordCheck" type="password" value="">
						
						<fieldset>
							<legend>성별</legend>
							<label>여</label> <input type="radio" name="gender" value="female" checked="checked">
							<label>남</label> <input type="radio" name="gender" value="male">
						</fieldset>
						
						<fieldset>
							<legend>약관동의</legend>
							<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
							<label>서비스 약관에 동의합니다.</label>
						</fieldset>
						
						<input type="submit" value="가입하기">
						
					</form>				
				</div><!-- /user -->
			</div><!-- /content -->
		</div><!-- /wrapper -->
		
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div> <!-- /container -->

</body>
</html>