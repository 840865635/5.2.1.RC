<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>计划管理</title>
		<%@include file="/common/iframe-meta.jsp" %>	
		<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
		<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
		<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
		<script type="text/javascript" src="${resourcesCtx}/js/format.js"></script>
		<script type="text/javascript" src="${resourcesCtx}/widgets/multiselect/jquery.multiselect.min.js"></script>
		<link   type="text/css" rel="stylesheet" href="${resourcesCtx}/widgets/multiselect/jquery.multiselect.css" />
		<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
		<script type="text/javascript" src="${resourcesCtx}/js/custom.tree.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#beginDate").datepicker({showButtonPanel:"true"});
				$("#endDate").datepicker({showButtonPanel:"true"});
				validatePlanForm();
			});
			
			function validatePlanForm(){
				$("#planForm").validate({
					submitHandler: function() {
						var result = iMatrix.getFormGridDatas("planForm","planItemsId");
						if(result){
							aa.submit('planForm','','mainZone',saveCallBack);
						}
					},
					rules: {
						"code": {
							required:true
						},
						"name": {
							required:true
						}
					},
					messages: {
						"code": {
							required:"必填"
						},
						"name": {
							required:"必填"
						}
					}
				});
			}

			function saveCallBack(){
				$("#beginDate").datepicker({showButtonPanel:"true"});
				$("#endDate").datepicker({showButtonPanel:"true"});
				validatePlanForm();
				getContentHeight();
			}
			function submitForm(url){
				$('#planForm').attr('action',url);
				$('#planForm').submit();
			}
			function changeFinishState(obj){
				$("#finished").attr("value",$(obj).attr("checked"));
			}

			//选择部门
			function selectDepartment(){
				var acsSystemUrl = "${ctx}";
				popTree({ title :'部门',
					innerWidth:'400',
					treeType:'DEPARTMENT_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:"departmentId",
					showInputId:"department",
					acsSystemUrl:acsSystemUrl,
					callBack:function(){}});
			}
			//选择上级部门
			function selectParentDepartment(){
				var acsSystemUrl = "${ctx}";
				popTree({ title :'部门',
					innerWidth:'400',
					treeType:'DEPARTMENT_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:"parentDepartmentId",
					showInputId:"parentDepartment",
					acsSystemUrl:acsSystemUrl,
					callBack:function(){}});
			}

			//选择顶级部门
			function selectTopDepartment(){
				var acsSystemUrl = "${ctx}";
				popTree({ title :'部门',
					innerWidth:'400',
					treeType:'DEPARTMENT_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:"topDepartmentId",
					showInputId:"topDepartment",
					acsSystemUrl:acsSystemUrl,
					callBack:function(){}});
			}

			//选择直属上级部门
			function selectSuperiorDepartment(){
				var acsSystemUrl = "${ctx}";
				popTree({ title :'部门',
					innerWidth:'400',
					treeType:'DEPARTMENT_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:"superiorDepartmentId",
					showInputId:"superiorDepartment",
					acsSystemUrl:acsSystemUrl,
					callBack:function(){}});
			}

			//选择工作组
			function selectWorkgroup(){
				var acsSystemUrl = "${ctx}";
				popTree({ title :'工作组',
					innerWidth:'400',
					treeType:'GROUP_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:"workgroupId",
					showInputId:"workgroup",
					acsSystemUrl:acsSystemUrl,
					callBack:function(){}});
			}

			//直属上级工作组
			function selectSuperiorWorkgroup(){
				var acsSystemUrl = "${ctx}";
				popTree({ title :'工作组',
					innerWidth:'400',
					treeType:'GROUP_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:"superiorWorkgroupId",
					showInputId:"superiorWorkgroup",
					acsSystemUrl:acsSystemUrl,
					callBack:function(){}});
			}

			
			//选择人员登录名
			function selectLoginName(){
				var acsSystemUrl = "${ctx}";
				popTree({ title :'选择人员',
					innerWidth:'400',
					treeType:'MAN_DEPARTMENT_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:"loginName",
					showInputId:"loginName",
					acsSystemUrl:acsSystemUrl,
					callBack:function(){selectLoginNameCallBack();}});
			}

			function selectLoginNameCallBack(){
				$("#loginName").attr("value",jstree.getLoginName());
			}

			//选择直属上级登录名
			function selectSuperiorLoginName(){
				var acsSystemUrl = "${ctx}";
				popTree({ title :'选择人员',
					innerWidth:'400',
					treeType:'MAN_DEPARTMENT_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:"superiorLoginName",
					showInputId:"superiorLoginName",
					acsSystemUrl:acsSystemUrl,
					callBack:function(){selectSuperiorLoginNameCallBack();}});
			}

			function selectSuperiorLoginNameCallBack(){
				$("#superiorLoginName").attr("value",jstree.getLoginName());
			}

			//选择角色
			function selectRole(){
				var acsSystemUrl = "${ctx}";
				custom_tree({url:webRoot+'/plan/select-role.htm',
					onsuccess:function(){selectRoleBack();},//回调方法
					width:500,
					height:320,
					title:'选择角色',
					nodeInfo:['id'],
					multiple:false,
					webRoot:acsSystemUrl
				});
			}
			function selectRoleBack(){
				var values =  getSelectValue('id')[0].split("_");
				if(values[0]=="_role") return;
				$("#roleId").attr("value",values[1]);;
				$("#role").attr("value",values[2]);;
			}

			//选择角色
			function selectSuperiorRole(){
				var acsSystemUrl = "${ctx}";
				custom_tree({url:webRoot+'/plan/select-role.htm',
					onsuccess:function(){selectSuperiorRoleBack();},//回调方法
					width:500,
					height:320,
					title:'选择角色',
					nodeInfo:['id'],
					multiple:false,
					webRoot:acsSystemUrl
				});
			}
			function selectSuperiorRoleBack(){
				var values =  getSelectValue('id')[0].split("_");
				if(values[0]=="_role") return;
				$("#superiorRoleId").attr("value",values[1]);;
				$("#superiorRole").attr("value",values[2]);;
			}


			//选择角色
			function roleClick(obj){
				var acsSystemUrl = "${ctx}";
				custom_tree({url:webRoot+'/plan/select-role.htm',
					onsuccess:function(){roleClickBack(obj);},//回调方法
					width:500,
					height:320,
					title:'选择相对条件',
					nodeInfo:['id'],
					multiple:false,
					webRoot:acsSystemUrl
				});

				
			}
			function roleClickBack(obj){
				var values =  getSelectValue('id')[0].split("_");
				if(values[0]=="_role") return;
				$("#"+obj.rowid+"_role").attr("value",values[2]);
				$("#"+obj.rowid+"_roleId").attr("value",values[1]);
			}

			//直属上级角色
			function superiorRoleClick(obj){
				var acsSystemUrl = "${ctx}";
				custom_tree({url:webRoot+'/plan/select-role.htm',
					onsuccess:function(){superiorRoleClickBack(obj);},//回调方法
					width:500,
					height:320,
					title:'选择相对条件',
					nodeInfo:['id'],
					multiple:false,
					webRoot:acsSystemUrl
				});

				
			}
			function superiorRoleClickBack(obj){
				var values =  getSelectValue('id')[0].split("_");
				if(values[0]=="_role") return;
				$("#"+obj.rowid+"_superiorRole").attr("value",values[2]);
				$("#"+obj.rowid+"_superiorRoleId").attr("value",values[1]);
			}


			//登录名
			function loginNameClick(obj){
				var acsSystemUrl = "${ctx}";
				popTree({ title :'选择人员',
					innerWidth:'400',
					treeType:'MAN_DEPARTMENT_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:obj.rowid+"_loginName",
					showInputId:obj.currentInputId,
					acsSystemUrl:acsSystemUrl,
					callBack:function(){loginNameClickBack(obj);}});
			}

			function loginNameClickBack(obj){
				$("#"+obj.rowid+"_loginName").attr("value",jstree.getLoginName());
			}

			//直属上级登录名
			function superiorLoginNameClick(obj){
				var acsSystemUrl = "${ctx}";
				popTree({ title :'选择人员',
					innerWidth:'400',
					treeType:'MAN_DEPARTMENT_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:obj.rowid+"_superiorLoginName",
					showInputId:obj.currentInputId,
					acsSystemUrl:acsSystemUrl,
					callBack:function(){superiorLoginNameClickBack(obj);}});
			}

			function superiorLoginNameClickBack(obj){
				$("#"+obj.rowid+"_superiorLoginName").attr("value",jstree.getLoginName());
			}
		</script>
	</head>
	<body onload="getContentHeight();">
	<div class="ui-layout-center">
		<div class="opt-body">
				<aa:zone name="mainZone">
					<div class="opt-btn">
						<button class='btn' onclick="submitForm('${ctx}/plan/save.htm')"><span><span>保存</span></span></button>
					</div>
					<div id="opt-content">
					<div id="message"><s:actionmessage theme="mytheme" /></div>	
					<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
						<form  id="planForm" name="planForm" method="post" action="">
							<input  name="id" id="id" value="${id }" type="hidden"/>
							<table class="form-table-border-left">
								<tr>
									<td>计划名称</td>
									<td><input id="name" name="name" value="${name }" /> </td>
									<td>计划编号</td>
									<td><input id="code" name="code" value="${code }"/> </td>
								</tr>
								<tr>
									<td>开始日期</td>
									<td>
										<input id="beginDate" name="beginDate" value="<s:date name="beginDate" format="yyyy-MM-dd"/>" readonly="readonly" />
									</td>
									<td>结束日期</td>
									<td>
										<input id="endDate" name="endDate" value="<s:date name="endDate" format="yyyy-MM-dd"/>" readonly="readonly" />
									</td>
								</tr>
								<tr>
									<td>计划数量</td>
									<td>
										<input id="amount" name="amount" value="${amount }" onkeyup="value=value.replace(/[^0-9]/g,'')"/>
									</td>
									<td>计划金额</td>
									<td>
										<input id="money" name="money" value="${money }"  />
									</td>
								</tr>
								<tr>
									<td>是否完成</td>
									<td>
										<input name="finished" id="finished" type="hidden" value="${finished }"></input><input id="ifFinished" <s:if test="finished">checked="checked"</s:if> type="checkbox" onclick="changeFinishState(this);"></input>
									</td>
									<td>计划状态</td>
									<td>
										<select id="planState" name="planState">
											<s:iterator value="@com.example.expense.base.enumeration.PlanState@values()" var="state">
												<option <s:if test="#state==planState">selected="selected"</s:if> value="${state}"><s:text name="%{code}"></s:text></option>
											</s:iterator>
										</select>
									</td>
								</tr>
								<tr>
									<td>用户登录名</td>
									<td>
										<input id="loginName" name="loginName" value="${loginName }" readonly="readonly" />
										<button onclick="selectLoginName()" type="button">选择</button>
									</td>
									<td>部门</td>
									<td>
										<input id="department" name="department" value="${department }" readonly="readonly" />
										<input type="hidden" id="departmentId" name="departmentId" value="${departmentId }" readonly="readonly" />
										<button onclick="selectDepartment()" type="button">选择</button>
									</td>
								</tr>
								<tr>
									<td>角色</td>
									<td>
										<input id="role" name="role" value="${role }"  readonly="readonly"/>
										<input type="hidden" id="roleId" name="roleId" value="${roleId }" readonly="readonly"/>
										<button onclick="selectRole()" type="button">选择</button>
									</td>
									<td>工作组</td>
									<td>
										<input id="workgroup" name="workgroup" value="${workgroup }"  readonly="readonly"/>
										<input type="hidden" id="workgroupId" name="workgroupId" value="${workgroupId }"  readonly="readonly"/>
										<button onclick="selectWorkgroup()" type="button">选择</button>
									</td>
								</tr>
								<tr>
									<td>上级部门</td>
									<td>
										<input id="parentDepartment" name="parentDepartment" value="${parentDepartment }" readonly="readonly"/>
										<input type="hidden" id="parentDepartmentId" name="parentDepartmentId" value="${parentDepartmentId }" readonly="readonly"/>
										<button onclick="selectParentDepartment()" type="button">选择</button>
									</td>
									<td>顶级部门</td>
									<td>
										<input id="topDepartment" name="topDepartment" value="${topDepartment }" readonly="readonly" />
										<input type="hidden" id="topDepartmentId" name="topDepartmentId" value="${topDepartmentId }" readonly="readonly" />
										<button onclick="selectTopDepartment()" type="button">选择</button>
									</td>
								</tr>
								<tr>
									<td>直属上级登录名</td>
									<td>
										<input id="superiorLoginName" name="superiorLoginName" value="${superiorLoginName }" readonly="readonly"/>
										<button onclick="selectSuperiorLoginName()" type="button">选择</button>
									</td>
									<td>直属上级部门</td>
									<td>
										<input id="superiorDepartment" name="superiorDepartment" value="${superiorDepartment }" readonly="readonly" />
										<input type="hidden" id="superiorDepartmentId" name="superiorDepartmentId" value="${superiorDepartmentId }" readonly="readonly" />
										<button onclick="selectSuperiorDepartment()" type="button">选择</button>
									</td>
								</tr>
								<tr>
									<td>直属上级角色</td>
									<td>
										<input id="superiorRole" name="superiorRole" value="${superiorRole }" readonly="readonly"/>
										<input type="hidden" id="superiorRoleId" name="superiorRoleId" value="${superiorRoleId }" readonly="readonly"/>
										<button onclick="selectSuperiorRole()" type="button">选择</button>
									</td>
									<td>直属上级工作组</td>
									<td>
										<input id="superiorWorkgroup" name="superiorWorkgroup" value="${superiorWorkgroup }" readonly="readonly" />
										<input type="hidden" id="superiorWorkgroupId" name="superiorWorkgroupId" value="${superiorWorkgroupId }" readonly="readonly" />
										<button onclick="selectSuperiorWorkgroup()" type="button">选择</button>
									</td>
								</tr>
							</table>
						</form>
							<grid:formGrid gridId="planItemsId" code="ES_PLAN_ITEM_EDIT" entity="${plan}" attributeName="planItems"></grid:formGrid>
					</div>
				</aa:zone>
			</div>
			</div>
	</body>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>