//新加行初始化数据
function packagingOperator(dataType,conditionId,data){
	var event = '';
	var doubleClickEvent = '';
	var enumEvent = '';
	//配置枚举或者键值对的时候
	if(data!=""){
		if(conditionId.indexOf("new")>=0){
			event = 'onclick="conditionValueEvent(\''+conditionId+'\')";';
		}else{
			event = 'onclick="conditionValueEvent('+conditionId+')";';
		}
	}
	//添加选择相对条件事件
	if(conditionId.indexOf("new")>=0){
		doubleClickEvent = 'ondblclick="selectRelativeCondition(\''+conditionId+'\')";';
	}else{
		doubleClickEvent = 'ondblclick="selectRelativeCondition('+conditionId+')";';
	}
	
	var result='<option role="option" value="">请选择</option>';
	var tdContent='';
	if(dataType == 'TEXT'){
		result+='<option value="ET">等于</option><option value="CONTAIN">包含</option><option value="NOT_CONTAIN">不包含</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input '+event+" "+doubleClickEvent+' id="'+conditionId+'_conditionValue" class="editable" onkeyup="validateFieldString(this,\''+conditionId+'\');" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}else if(dataType == 'ENUM'){
		result+='<option value="ET">等于</option><option value="NET">不等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input '+event+' id="'+conditionId+'_conditionValue" class="editable"  type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}else if(dataType == 'BOOLEAN'){
		result+='<option value="ET">等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input '+event+' id="'+conditionId+'_conditionValue" class="editable" onkeyup="validateFieldString(this);" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}else if(dataType == 'NUMBER' || dataType == 'AMOUNT' || dataType == 'DOUBLE' || dataType == 'FLOAT'){
		result+='<option value="ET">等于</option><option value="GT">大于</option><option value="LT">小于</option><option value="GET">大于等于</option><option value="LET">小于等于</option><option value="NET">不等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input id="'+conditionId+'_conditionValue" class="editable" onkeyup="value=value.replace(/[^0-9\.]/g,\'\');" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}else if(dataType == 'INTEGER'){
		result+='<option value="ET">等于</option><option value="GT">大于</option><option value="LT">小于</option><option value="GET">大于等于</option><option value="LET">小于等于</option><option value="NET">不等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input id="'+conditionId+'_conditionValue" class="editable" onkeyup="value=value.replace(/[^0-9]/g,\'\');" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}else if(dataType == 'DATE'){
		result+='<option value="ET">等于</option><option value="GT">大于</option><option value="LT">小于</option><option value="GET">大于等于</option><option value="LET">小于等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input id="'+conditionId+'_conditionValue" class="editable" readonly="readonly" type="text" name="conditionValue" style="width: 98%;" role="textbox">'
		+'<script type="text/javascript">'
        +'$("#'+conditionId+'_conditionValue").datepicker({'
        +'"dateFormat":"yy-mm-dd",'
		    +'  changeMonth:true,'
		     +' changeYear:true,'
			+'	showButtonPanel:"true"'
        +'});'                                       
     +'</script>';
	}else if(dataType == 'TIME'){
		result+='<option value="ET">等于</option><option value="GT">大于</option><option value="LT">小于</option><option value="GET">大于等于</option><option value="LET">小于等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input id="'+conditionId+'_conditionValue" readonly="readonly" class="editable" type="text" name="conditionValue" style="width: 98%;" role="textbox">'
		+'<script type="text/javascript">'
        +'$("#'+conditionId+'_conditionValue").datetimepicker({'
		    +'"dateFormat":"yy-mm-dd",'
			 +'      changeMonth:true,'
			  +'     changeYear:true,'
			  +'     showSecond: false,'
				+'	showMillisec: false,'
				+'	"timeFormat": "hh:mm"'
        +'});'
        +'</script>';
	}else if(dataType == 'LONG'){
		result+='<option value="ET">等于</option><option value="GT">大于</option><option value="LT">小于</option><option value="GET">大于等于</option><option value="LET">小于等于</option><option value="NET">不等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input ' +doubleClickEvent+'  id="'+conditionId+'_conditionValue" class="editable" onkeyup="value=value.replace(/[^0-9]/g,\'\');" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}
	window.parent.$("#"+conditionId+"_operator").html(result);
	window.parent.$("#"+conditionId+"_conditionValue").parent().html(tdContent);
}

//修改数据类别初始化已有数据
function packagingOperatorUpdate(dataType,conditionId,tableId){
	var event = '';
	var doubleClickEvent = '';
	var enumEvent = '';
	//添加选择相对条件事件，配置枚举或者键值对的时候
	if(conditionId.indexOf("new")>=0){
		event = 'onclick="conditionValueEvent(\''+conditionId+'\')";';
		doubleClickEvent = 'ondblclick="selectRelativeCondition(\''+conditionId+'\')";';
	}else{
		event = 'onclick="conditionValueEvent('+conditionId+')";';
		doubleClickEvent = 'ondblclick="selectRelativeCondition('+conditionId+')";';
	}
	
	var result='<option role="option" value="">请选择</option>';
	var tdContent='';
	if(dataType == 'TEXT'){
		result+='<option value="ET">等于</option><option value="CONTAIN">包含</option><option value="NOT_CONTAIN">不包含</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input '+event+" "+doubleClickEvent+'  id="'+conditionId+'_conditionValue" class="editable" onkeyup="validateFieldString(this,'+conditionId+');" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}else if(dataType == 'ENUM'){
		result+='<option value="ET">等于</option><option value="NET">不等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input '+event+' id="'+conditionId+'_conditionValue" class="editable"  type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}else if(dataType == 'BOOLEAN'){
		result+='<option value="ET">等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input '+event+' id="'+conditionId+'_conditionValue" class="editable" onkeyup="validateFieldString(this);" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}else if(dataType == 'NUMBER' || dataType == 'AMOUNT' || dataType == 'DOUBLE' || dataType == 'FLOAT'){
		result+='<option value="ET">等于</option><option value="GT">大于</option><option value="LT">小于</option><option value="GET">大于等于</option><option value="LET">小于等于</option><option value="NET">不等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input id="'+conditionId+'_conditionValue" class="editable" onkeyup="value=value.replace(/[^0-9\.]/g,\'\');" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}else if(dataType == 'INTEGER' ){
		result+='<option value="ET">等于</option><option value="GT">大于</option><option value="LT">小于</option><option value="GET">大于等于</option><option value="LET">小于等于</option><option value="NET">不等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input id="'+conditionId+'_conditionValue" class="editable" onkeyup="value=value.replace(/[^0-9]/g,\'\');" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}else if(dataType == 'DATE'){
		result+='<option value="ET">等于</option><option value="GT">大于</option><option value="LT">小于</option><option value="GET">大于等于</option><option value="LET">小于等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input id="'+conditionId+'_conditionValue" class="editable" readonly="readonly" type="text" name="conditionValue" style="width: 98%;" role="textbox">'
		+'<script type="text/javascript">'
        +'$("#'+conditionId+'_conditionValue").datepicker({'
		    +'  changeMonth:true,'
		     +' changeYear:true,'
			+'	showButtonPanel:"true"'
        +'});'                                       
     +'</script>';
	}else if(dataType == 'TIME'){
		result+='<option value="ET">等于</option><option value="GT">大于</option><option value="LT">小于</option><option value="GET">大于等于</option><option value="LET">小于等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input id="'+conditionId+'_conditionValue" readonly="readonly" class="editable" type="text" name="conditionValue" style="width: 98%;" role="textbox">'
		+'<script type="text/javascript">'
        +'$("#'+conditionId+'_conditionValue").datetimepicker({'
		    +'"dateFormat":"yy-mm-dd",'
			 +'      changeMonth:true,'
			  +'     changeYear:true,'
			  +'     showSecond: false,'
				+'	showMillisec: false,'
				+'	"timeFormat": "hh:mm"'
        +'});'
        +'</script>';
	}else if(dataType == 'LONG'){
		result+='<option value="ET">等于</option><option value="GT">大于</option><option value="LT">小于</option><option value="GET">大于等于</option><option value="LET">小于等于</option><option value="NET">不等于</option><option value="IS_NULL">为空</option><option value="NOT_NULL">不为空</option>';
		tdContent='<input ' +doubleClickEvent+'  id="'+conditionId+'_conditionValue" class="editable" onkeyup="value=value.replace(/[^0-9]/g,\'\');" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	}
	var operator=$("#"+conditionId+"_operator").val();
	var conditionValue=$("#"+conditionId+"_conditionValue").val();
	$("#"+conditionId+"_operator").html(result);
	if(operator != '')
		$("#"+conditionId+"_operator").attr("value",operator);
	$("#"+conditionId+"_conditionValue").parent().html(tdContent);
	if(conditionValue != '')
		$("#"+conditionId+"_conditionValue").attr("value",conditionValue);
}


//弹框选值后，继续初始化列表事件
function setOperatorValue(conditionId,value,dataType){
	var event = '';
	if(conditionId.indexOf("new")>=0){
		event = 'onclick="conditionValueEvent(\''+conditionId+'\')";';
	}else{
		event = 'onclick="conditionValueEvent('+conditionId+')";';
	}
	var tdContent='<input '+event+' id="'+conditionId+'_conditionValue" value="'+value+'"  class="editable" onkeyup="validateFieldString(this,'+conditionId+');" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	window.parent.$("#"+conditionId+"_conditionValue").parent().html(tdContent);
}

//选择相对条件
function selectRelativeCondition(conditionId){
	custom_tree({url:webRoot+'/authority/data-rule-selectRelativeCondition.htm',
		onsuccess:function(){selectConditionCallBack(conditionId+"",getSelectValue('id'));},//回调方法
		width:500,
		height:320,
		title:'选择相对条件',
		nodeInfo:['id'],
		multiple:false,
		webRoot:imatrixRoot
	});
}
function selectConditionCallBack(conditionId,value){
	var values = value[0].split("*");
	if(values[0]=="root") return;
	var doubleClickEvent = '';
	var event = '';
	if(conditionId.indexOf("new")>=0){
		event = 'onclick="conditionValueEvent(\''+conditionId+'\')";';
		doubleClickEvent = 'ondblclick="selectRelativeCondition(\''+conditionId+'\')";';
	}else{
		event = 'onclick="conditionValueEvent('+conditionId+')";';
		doubleClickEvent = 'ondblclick="selectRelativeCondition('+conditionId+')";';
	}
	var tdContent='<input '+event+" "+doubleClickEvent+' id="'+conditionId+'_conditionValue" value="'+values[2]+'"  class="editable" onkeyup="validateFieldString(this,'+conditionId+');" type="text" name="conditionValue" style="width: 98%;" role="textbox">';
	$("#"+conditionId+"_conditionValue").parent().html(tdContent);
	jQuery("#conditionGrid").jqGrid('setCell',conditionId,"relativeType",values[1]);
}