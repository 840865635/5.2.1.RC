<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

	<div id="myExpenseReport" class="west-notree"  ><a href="${ctx }/expense-report/list.htm">我的报销单</a></div>
	<div id="report" class="west-notree"  ><a href="${ctx }/report/list.htm">报销单子流程</a></div>
	<div id="order1" class="west-notree" ><a href="${ctx }/order/list.htm">订单(主子表)</a></div>
	<div id="order2" class="west-notree"  ><a href="${ctx }/order/dynamic-list.htm">订单(动态列表)</a></div>
	<div id="order3" class="west-notree" ><a href="${ctx }/order/api-list.htm">订单(列表API)</a></div>
	<div id="order4" class="west-notree" ><a href="${ctx }/order/control-list.htm">订单(控制行拖动)</a></div>
	<div id="template" class="west-notree"  ><a href="${ctx }/template/list.htm">模板(自定义列表)</a></div>
	<div id="product" class="west-notree"  ><a href="${ctx }/emsproduct/list.htm">产品(增删改)</a></div>
	<div id="productApi" class="west-notree" ><a href="${ctx }/emsproduct/api-list.htm">产品(列表API)</a></div>
	<div id="productAllPageTotal" class="west-notree" ><a href="${ctx }/emsproduct/total-list.htm">产品(合计所有页)</a></div>
	<div id="customSearch" class="west-notree" ><a href="${ctx }/emsproduct/custom-search-list.htm">产品(自定义查询)</a></div>
	<div id="cellMerge" class="west-notree" ><a href="${ctx }/emsproduct/cell-merge-list.htm">产品(单元格合并)</a></div>
	<div id="groupHeader" class="west-notree" ><a href="${ctx }/emsproduct/group-header-list.htm">产品(表头组合)</a></div>
	<div id="queryEvent" class="west-notree" ><a href="${ctx }/emsproduct/query-event-list.htm">产品(查询事件)</a></div>
	<div id="product-event" class="west-notree"  ><a href="${ctx }/emsproduct/list-event.htm">产品(列表事件)</a></div>
	<div id="plan" class="west-notree" ><a href="${ctx }/plan/list.htm">计划管理(数据权限)</a></div>
	<div id="product-dataRule" class="west-notree" ><a href="${ctx }/emsproduct/dataRule-list.htm">产品(数据类别api)</a></div>
	<div id="orderCombineList" class="west-notree" ><a href="${ctx }/order/order-combine-list.htm">订单组合列表</a></div>
	<div id="messageNotice" class="west-notree" ><a href="${ctx }/expense-report/expense-message.htm">系统消息提示</a></div>
	<div class="linee"></div>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>