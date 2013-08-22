<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/mms-taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
	<title>数据表</title>
	<%@ include file="/common/mms-iframe-meta.jsp"%>
	<script type="text/javascript">
		$(document).ready(function(){
			create_tree();
		});
		function create_tree(){
			var id = $("#id").attr("value");
			var itemType=$("#itemType").val();
			$.ajaxSetup({cache:false});
			$("#treeDiv").bind("select_node.jstree",function(e){
				//selectNode($("#"+treeId).find(".jstree-clicked").parent().attr("id"),treeId);
			}).jstree({
				"json_data":{
						"ajax" : { "url" : webRoot+"/authority/permission-item-tree.htm?id="+id+"&itemType="+itemType,
							"data" : function (n) {  
								return { currentTreeId : n!=-1 ? n.attr("id") : 0 };   
							}
						}
			   },
			   "themes" : {  
					  "theme" : "classic",  
					  "dots" : true,  
					  "icons" : true 
					 },
			 "types" :{
					"types" : {
						"company" : {
							"icon" : {
								"image" : resourceRoot+"/widgets/jstree/themes/root.gif"
							}
						},
						"folder" : {
							"icon" : {
								"image" : resourceRoot+"/widgets/jstree/themes/folder.gif"
							}
						},
						"user" : {
							"icon" : {
								"image" : resourceRoot+"/widgets/jstree/themes/file.gif"
							}
						}
					}
				 }, 
			 "ui":{ "select_multiple_modifier" : "alt"},
			 "plugins" : [ "themes", "json_data","types","ui"]
				});
		}

		function select(){
			if(typeof($("#treeDiv").find(".jstree-clicked").parent().attr("id"))=='undefined') {
				alert("请选择正确的节点！"); return;
			}
			var values = $("#treeDiv").find(".jstree-clicked").parent().attr("id").split("_");;
			var itemType=$("#itemType").val();
			var rowId = $("#rowId").val();
			if(itemType=="USER" || itemType=="CURRENT_USER_LOGINNAME"|| itemType=="CURRENT_USER_DIRECT_SUPERIOR_LOGINNAME" ){
				if(values[0]=="allUser" || values[0]=="department" || values[0]=="allField"){
					alert("请选择人员或表单字段！"); return;
				}
			}else if(itemType=="DEPARTMENT" || itemType=="CURRENT_USER_DEPARTMENT"|| itemType=="CURRENT_USER_SUPERIOR_DEPARTMENT" || itemType=="CURRENT_USER_TOP_DEPARTMENT" || itemType=="CURRENT_USER_DIRECT_SUPERIOR_DEPARTMENT"){
				if(values[0]=="allDepartment" || values[0]=="allField"){
					alert("请选择部门或表单字段！"); return;
				}
			}else if(itemType=="ROLE" || itemType=="CURRENT_USER_ROLE" || itemType=="CURRENT_USER_DIRECT_SUPERIOR_ROLE"){
				if(values[0]=="allRole" || values[0]=="allField"){
					alert("请选择角色或表单字段！"); return;
				}
			}else if(itemType=="WORKGROUP" || itemType=="CURRENT_USER_WORKGROUP" || itemType=="CURRENT_USER_DIRECT_SUPERIOR_WORKGROUP"){
				if(values[0]=="allWorkgroup" || values[0]=="allField"){
					alert("请选择工作组或表单字段！"); return;
				}
			}
			window.parent.$("#"+rowId+"_conditionValue").attr("value",values[1]);
			window.parent.$("#"+rowId+"_conditionName").attr("value",values[2]);
			window.parent.$.colorbox.close();
		}
	</script>
	<style type="text/css">
	.form-table-without-border td input{
		width:200px;
	}
	</style>
</head>
<body onload="getContentHeight();">
<div class="ui-layout-center">
<div class="opt-body">
	<form id="defaultForm" name="defaultForm"action="" method="post" >
		<input type="hidden" id="id" value="${id }">
		<input type="hidden" id="itemType" value="${itemType }">
		<input type="hidden" id="rowId" value="${rowId }">
	</form>
	<aa:zone name="main_zone">
		<div class="opt-btn">
			<button class="btn" onclick="select();"><span><span >确定</span></span></button>
		</div>
		<div id="opt-content">
			<div id="treeDiv" class="demo"></div>
		</div>
	</aa:zone>
</div>
</div>
</body>
</html>
