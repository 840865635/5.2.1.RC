<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>订单管理</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/myMessage.js"></script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="expenseReport";
		var thirdMenu="order3";
	</script>
	
	<%@ include file="/menus/header.jsp" %>

	<%@ include file="/menus/expense-sec-menu.jsp" %>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/expense-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
					</div>
					<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
					
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post"  action="">
							<table id="apiTable"></table>
							<div id="apiTable_pager"></div> 
							<script type="text/javascript">
								$(document).ready(function(){
									jQuery("#apiTable").jqGrid({
										url: '${ctx}/order/grid-column.htm',
										pageName: 'page',
										datatype: "json", 
										colNames:${gridColumnInfo.colNames}, 
										colModel:${gridColumnInfo.colModel}, 
										rowNum: 20, 
									   	autowidth: true,
									   	pager: '#apiTable_pager', 
										viewrecords: true, 
										sortorder: "desc",
										multiselect: true,
										postData: { _list_code: "ES_ORDER" }
									}).navGrid('#apiTable_pager',{edit:false,add:false,del:false,search:false});
									
									var h= $("#opt-content").height();
									jQuery("#apiTable").jqGrid("setGridHeight",h-50);
					     	  	});
							</script>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>