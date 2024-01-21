<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="kfs" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="asset"  uri="/WEB-INF/asset-tags/asset.tld"%>
    
<!DOCTYPE html>
<html>
<head>
<!-- =================================================== -->
<jsp:include page="../../common/meta_css.jsp" flush="false" />
<!-- =================================================== -->
<title><c:out value="${pageTitle}" default="자산운용교육" /></title>
</head>
<body>
<!-- =================================================== -->
<jsp:include page="../../common/header.jsp" flush="false" />
<!-- =================================================== -->
<main class="container mx-3 my-3">
  
  <h1>사용자 List11</h1>
  <table class="table">
  <thead class="thead-light">
    <tr>
      <th scope="col">#</th>
      <th scope="col">사용자Id</th>
      <th scope="col">사용자명</th>
      <th scope="col">동작 <a href="/admin/user/insert" class="btn btn-info"><span><i class="fa-solid fa-user-plus"></i></span>추가</a></th>
    </tr>
  </thead>
  <tbody>
  	<c:forEach var="user" items="${list}" varStatus="status"> 
	    <tr>
	      <th scope="row">${status.count }</th>
	      <td>${user.sys01UserId }</td>
	      <td>${user.sys01UserNm }</td>
	      <td>
	      <button class="btn btn-primary btnModify" data-user-id="${user.sys01UserId }"><span><i class="fa-regular fa-pen-to-square"></i></span>수정</button>
	      <button class="btn btn-secondary btnDelete"  data-user-id="${user.sys01UserId }" data-user-nm="${user.sys01UserNm }"><span><i class="fa-regular fa-trash-can"></i></span>삭제</button>
	      </td>
	    </tr>
    </c:forEach>
  </tbody>
</table>
</main>
<!-- =================================================== -->
<jsp:include page="../../common/footer.jsp" flush="false" />
<!-- -================================================== -->
<form id="form1" method="GET" action="">
	<input type="hidden" name="sys01UserId"/>
	<input type="hidden" name="sys01UserNm"/>
</form>
<script>
$(document).ready(function () {
	console.log('ready...');
	$('.btnModify').on('click', function(){
		var userId = $(this).data('user-id');
		$('#form1 [name=sys01UserId]').val(userId);
		$('#form1').attr('action', '/admin/user/update');
		$('#form1').submit();
	})
	$('.btnDelete').on('click', function(){
		var userId = $(this).data('user-id');
		var userNm = $(this).data('user-nm');
		if(confirm("사용자 " + userNm + "을(를) 삭제하시겠습니까?")){
			$('#form1 [name=sys01UserId]').val(userId);
			$('#form1 [name=sys01UserNm]').val(userId);
			$('#form1').attr('action', '/admin/user/delete');
			$('#form1').submit();
		}
		
	})
});
</script>
</body>
</html>