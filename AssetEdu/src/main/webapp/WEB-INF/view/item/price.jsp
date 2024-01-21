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
<jsp:include page="../common/meta_css.jsp" flush="false" />
<!-- =================================================== -->
<title><c:out value="${pageTitle}" default="시세입력" /></title>
<style>
#stdDate {
	width :120px;
}
.table tbody tr.highlight td {
  background-color: #EAF0F7;
}
</style>
</head>
<body>
<!-- =================================================== -->
<jsp:include page="../common/header.jsp" flush="false" />
<!-- =================================================== -->
<main class="container mx-3 my-3">
	<h2><i class="fa-solid fa-cube my-3"></i> 보유주식 평가단가 입력</h2>
  
	<div class="container-lg p-3 border border-2 rounded-1">
		<span class="align-middle fw-bold">기준일자 </span> 
		<input type="text" class="form-control d-inline align-middle text-center dateinput" id="stdDate" name="stdDate" value="${stdDate}"/>
		<input type="text" class="form-control w-50 d-inline align-middle" placeholder="검색어(종목코드/종목명)를 입력하세요" id="searchText" name="searchText" value="${param.searchText}">
		<a class="btn d-inline align-middle btn-primary btnRetrieve"><i class="fa-solid fa-search"></i> 조회</a>
        <a class="btn d-inline align-middle btn-secondary btnInit"><i class="fa-solid fa-backspace"></i> 초기화</a>
	</div>

	<table class="table table-hover table-sm mt-3 priceTable" style="font-size:small">
	  <thead class="table-light">
	    <tr class="text-center">
	      <th scope="col" style="width:30px">No</th>
	      <th scope="col" style="width:120px">기준일자</th>
          <th scope="col" style="width:200px">종목코드</th>
	      <th scope="col">종목명</th>
	      <th scope="col" style="width:150px">평가단가</th>
          <th scope="col" style="width:75px"> </th>
	    </tr>
	  </thead>
	  <tbody class="table-group-divider" >
	  	<c:forEach var="price" items="${list}" varStatus="status">
		    <tr class="align-middle">
		      <td scope="row" class="text-center fw-bold">${status.count}</td>
		      <td class="itm02ApplyDate text-center"><asset:displayYmd ymd="${price.itm02ApplyDate}"/></td>
		      <td class="itm02ItemCd text-center">${price.itm02ItemCd}</td>
		      <td class="itm02ItemNm">${price.itm02ItemNm}</td>
              <td class="itm02ApplyPrice text-end"><fmt:formatNumber value="${price.itm02ApplyPrice}" pattern="#,###"/></td>
              <td align="right">
                  <button class="btn btn-primary btn-sm btnModify" data-bs-target="#modiPrice"><span><i class="fa-regular fa-pen-to-square"></i></span></button>
              </td>
		    </tr>
	    </c:forEach>
	  </tbody>
	</table>
</main>

<!-- 단가입력 Modal -->
<div class="modal fade" id="modiPrice" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable" role="document">
    <div class="modal-content">
      <form method="POST" id="formModiPrice" class="validcheck">
        <div class="modal-header">
            <h5 class="modal-title"><b>평가단가 입력</b></h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
            <div class="mb-3">
                <label for="itm02ApplyDate" class="form-label fw-bold">기준일자</label>
                <input type="text" class="form-control dateinput" name="itm02ApplyDate" />
            </div>
            <div class="mb-3">
                <label for="itm02ItemCd" class="form-label fw-bold">종목코드</label>
                <input type="text" class="form-control" name="itm02ItemCd" />
            </div>
            <div class="mb-3">
                <label for="itm02ItemNm" class="form-label fw-bold">종목명</label>
                <input class="form-control" name="itm02ItemNm" />
            </div>
            <div class="mb-3">
                <label for="itm02ApplyPrice" class="form-label fw-bold" >평가단가</label>
                <input class="form-control text-end amtinput" name="itm02ApplyPrice" required pattern="[0-9,]+" />
            </div>
        </div>
        <div class="modal-footer d-flex justify-content-center">
            <button type="button" class="btn btn-primary" id="btnUpdatePrice">저장</button>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- =================================================== -->
<jsp:include page="../common/footer.jsp" flush="false" />
<!-- -================================================== -->
<script type="text/javascript" src="/js/input-format.js"></script>

<script>
$(document).ready(function () {
	console.log('시세입력 종목리스트...ready');
	
	let validateChecker = $('form.validcheck').jbvalidator({
        language: '/js/validation/lang/ko.json'
    });
	
	//화면뜨면 검색창에 포커스 
	$("#searchText").focus();
	
	//검색창에서 enter이면 조회
	$("#searchText").on("keyup",function(key){
        if(key.keyCode==13) { //enter
        	$('.btnRetrieve').trigger('click');
        }else if(key.keyCode == 46){ //DEL
        	$(this).val('');
        }
	});	
	
    //amt format set
    $('input.amtinput').inputMoneyFormat();
    
    //date format set
    $('input.dateinput').inputDateFormat();
	
	//보유일자 datepicker셋팅
    $('#stdDate')
    .datepicker({
       format: 'yyyy-mm-dd', //데이터 포맷 형식(yyyy : 년 mm : 월 dd : 일 )
       autoclose: true,
       todayHighlight: true,
       language: 'ko' //달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.
    })
    .on('changeDate', function (e) {
       console.log(e);
    });

    //테이블 클릭시 하이라이트 표시
    $('.priceTable').on('click', 'tbody tr', function(event) {
          $(this).addClass('highlight').siblings().removeClass('highlight');
    });
	
	//조회버튼
	$('.btnRetrieve').on('click', function(){
		var stdDate = $('#stdDate').val();
		var data = {
			searchText : $('#searchText').val(), 
			stdDate : $('#stdDate').val()
		}
		AssetUtil.submitGet('/item/price', data );	
	});
	
	//초기화
	$('.btnInit').on('click', function(){
		var data = {
				searchText : null, 
				stdDate : AssetUtil.today()
			}
		AssetUtil.submitGet('/item/price', data );	
	});
	
	//단가입력 modal show form
	$('.btnModify').on('click', function(){
		console.log('단가입력 shown');
		
		$modal = $('#modiPrice');
		$tr = $(this).closest('tr');

        var val = $tr.find('.itm02ApplyDate').text();
        $modal.find('input[name=itm02ApplyDate]').val(val).attr('readonly',true);

        var val = $tr.find('.itm02ItemCd').text();
        $modal.find('input[name=itm02ItemCd]').val(val).attr('readonly',true);

        var val = $tr.find('.itm02ItemNm').text();
        $modal.find('input[name=itm02ItemNm]').val(val).attr('readonly',true);

        var val = $tr.find('.itm02ApplyPrice').text();
        $modal.find('input[name=itm02ApplyPrice]').val(val);
        
        $('#modiPrice').modal('show');
	});
	
	//단가저장 submit
	$('#btnUpdatePrice').on('click', function(){
		var $modal = $('#modiPrice');
		var $form = $('#formModiPrice');
		
        var validateChecker = $form.jbvalidator({
            language: '/js/validation/lang/ko.json'
        });
        var errorCount = validateChecker.checkAll($form);
        if(errorCount > 0)return;
	
        var amt = $('input[name=itm02ApplyPrice]').val();
        $('input[name=itm02ApplyPrice]').val(AssetUtil.toNumber(amt));
        
        var json = AssetUtil.formToJson($('#formModiPrice'));
        json = JSON.stringify(json);
        AssetUtil.ajax('/item/price_update', json, {method:'POST', success:(response)=>{
        	if(response.result == 'OK'){
        		alert(response.msg);
        		AssetUtil.submitGet('/item/price');
        	} else {
        		alert("저장실패 " + response.result);
        	}
        }});
	});
	
}); 
</script>
</body>
</html>