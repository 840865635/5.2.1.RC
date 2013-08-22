package com.norteksoft.mms.authority.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.norteksoft.bs.options.dao.OptionGroupDao;
import com.norteksoft.bs.options.entity.Option;
import com.norteksoft.bs.options.entity.OptionGroup;
import com.norteksoft.mms.authority.dao.ConditionDao;
import com.norteksoft.mms.authority.dao.DataRuleDao;
import com.norteksoft.mms.authority.dao.PermissionDao;
import com.norteksoft.mms.authority.dao.PermissionBaseDao.ConditionResult;
import com.norteksoft.mms.authority.entity.Condition;
import com.norteksoft.mms.authority.entity.DataRule;
import com.norteksoft.mms.authority.entity.Permission;
import com.norteksoft.mms.authority.enumeration.FieldOperator;
import com.norteksoft.mms.authority.enumeration.LogicOperator;
import com.norteksoft.mms.base.utils.PermissionUtils;
import com.norteksoft.mms.form.dao.DataTableDao;
import com.norteksoft.mms.form.dao.TableColumnDao;
import com.norteksoft.mms.form.entity.DataTable;
import com.norteksoft.mms.form.entity.TableColumn;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.mms.module.dao.MenuDao;
import com.norteksoft.mms.module.entity.Menu;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Service
@Transactional
public class DataRuleManager {
	@Autowired
	private DataRuleDao dataRuleDao;
	@Autowired
	private ConditionDao conditionDao;
	@Autowired
	private DataTableDao dataTableDao;
	@Autowired
	private TableColumnDao tableColumnDao;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private OptionGroupDao optionGroupDao;
	
	private final String NO_VALUE_STRING= "NO_VALUE_#~_+~=%";

	/**
	 * 根据id获得数据规则
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public DataRule getDataRule(Long id) {
		return dataRuleDao.get(id);
	}

	/**
	 * 获得所有数据规则
	 * @param page
	 */
	@Transactional(readOnly=true)
	public void getDataRulePage(Page<DataRule> page) {
		dataRuleDao.getDataRulePage(page);
	}

	/**
	 * 保存数据规则
	 * @param dataRule
	 */
	public void saveDataRule(DataRule dataRule) {
		dataRuleDao.save(dataRule);
		List<Condition> conditions=new ArrayList<Condition>();
		List<Object> objects=JsonParser.getFormTableDatas(Condition.class);
		for(Object obj:objects){
			Condition condition=(Condition)obj;
			condition.setDataRule(dataRule);
			conditionDao.save(condition);
			conditions.add(condition);
		}
		if(conditions.size()>0){
			dataRule.setConditions(conditions);
		}
	}
	
	public List<DataRule> getDataRuleByDataTable(Long tableId){
		return dataRuleDao.getDataRuleByDataTable(tableId);
	}

	/**
	 * 删除数据规则且该规则下的所有条件
	 * @param ids
	 */
	public void deleteDataRule(String ids) {
		for(String id:ids.split(",")){
			List<Permission> list = permissionDao.getPermissionsByDataRule(Long.valueOf(id));
			for(Permission p:list){
				p.setDataRule(null);
				permissionDao.delete(p);
			}
			//permissionDao.deletePermissionByDataRuleId(Long.valueOf(id));
			conditionDao.deleteConditionByRuleId(Long.valueOf(id));
			dataRuleDao.delete(Long.valueOf(id));
		}
	}

	/**
	 * 根据编号获得规则
	 * @param code
	 * @return
	 */
	@Transactional(readOnly=true)
	public DataRule getDataRuleByCode(String code) {
		return dataRuleDao.getDataRuleByCode(code);
	}

	/**
	 * 根据编号和ID获得编号相同且ID不同的规则
	 * @param code
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public DataRule getDataRuleByCode(String code, Long id) {
		return dataRuleDao.getDataRuleByCode(code,id);
	}

	/**
	 * 获得所有启用的数据表
	 * @return
	 */
	public void findAllEnabledDataTable(Page<DataTable> page) {
		dataTableDao.findAllEnabledDataTable(page);
	}

	/**
	 * 根据数据表id获得字段
	 * @param tableColumnPage
	 * @param tableId
	 */
	public void getTableColumnByDataTableId(Page<TableColumn> tableColumnPage,Long dataTableId) {
		tableColumnDao.getTableColumnByDataTableId(tableColumnPage, dataTableId);
	}
	
	/**
	 * 根据规则类型查询数据规则
	 * @param ruleTypeId
	 * @return
	 */
	public List<DataRule> getDataRulesByRuleType(Long ruleTypeId){
		return dataRuleDao.getDataRulesByRuleType(ruleTypeId);
	}

	/**
	 * 根据规则类型查询数据规则
	 * @param page
	 * @param ruleTypeId
	 */
	public void getDataRulesByRuleType(Page<DataRule> page, Long ruleTypeId) {
		dataRuleDao.getDataRulesByRuleType(page,ruleTypeId);
	}
	
	/**
	 * 根据菜单查询数据规则
	 * @param page
	 * @param ruleTypeId
	 */
	public void getDataRulesByMenuId(Page<DataRule> page, Long menuId) {
		dataRuleDao.getDataRulesByMenuId(page,menuId);
	}
	
	/**
	 * 根据菜单查询数据规则
	 * @param page
	 * @param ruleTypeId
	 */
	public List<DataRule> getDataRulesByMenuId(Long menuId) {
		return  dataRuleDao.getDataRulesByMenuId(menuId);
	}
	/**
	 * 验证删除
	 * @param ids
	 * @return
	 */
	public String validateDelete(String ids) {
		String result="";
		for(String id:ids.split(",")){
			List<Permission> permissions=permissionDao.getPermissionsByDataRule(Long.valueOf(id));
			if(permissions != null && permissions.size()>0){
				DataRule dataRule=dataRuleDao.get(Long.valueOf(id));
				if(StringUtils.isNotEmpty(result))
					result+="、";
				result+=dataRule.getName();
			}
		}
		if(StringUtils.isNotEmpty(result))
			result="名称为："+result+" 的数据规则中有数据授权，确定删除吗？";
		return result;
	}
	/**
	 * 根据menuId获得启用的数据表
	 * @return
	 */
	public List<DataTable> getEnabledDataTableByMenuId(Long menuId) {
		return  dataTableDao.getEnabledDataTableByMenuId(menuId);
	}
	
	public ConditionResult getConditionResult(String hql,List<String> ruleCodes, LogicOperator link,Object... prmts) {
			
			String alias = dataRuleDao.getAlias(hql); //HQL实体别名 
			StringBuilder newhql=new StringBuilder();
			List<Object> prameters=new ArrayList<Object>();
			for(Object o:prmts){
				prameters.add(o);
			}
			int t = 0;
			List<DataRule> rules = new ArrayList<DataRule>();
			for (String code : ruleCodes) {
				DataRule r = dataRuleDao.getDataRuleByCode(code);
				if(r!=null&& r.getConditions().size()>0) rules.add(r);
			}
			for (DataRule rule : rules) {
				int i = 0;
				List<Condition> conditions  = rule.getConditions();
				newhql.append("(");
				for(Condition c:conditions){
					newhql.append(PermissionUtils.getLeftBracket(c.getLeftBracket()));
					String value = c.getConditionValue();
					String con = "",subCon = "";
					//如果是相对条件，解析相对条件
					if(c.getRelativeType()!=null){
						value = PermissionUtils.getValueByRelativeType(c.getRelativeType());
						if(StringUtils.isEmpty(value)) value = NO_VALUE_STRING;
						String[] values =  value.split(",");
						int a = 0;
						newhql.append("(");
						for (String val : values) {
							value = val;
							if(c.getField().contains("$")){//解析子表字段
								subCon = c.getField().substring(1, c.getField().length());
								newhql.append(subCon);
							}else{
								con = alias+"."+c.getField();
								newhql.append(con);
							}
							if(c.getField().contains("$")){
								newhql.append(PermissionUtils.dealTextNullCondition(c.getDataType(), c.getOperator(), subCon));
							}else{
								newhql.append(PermissionUtils.dealTextNullCondition(c.getDataType(), c.getOperator(), con));
							}
							if(c.getDataType()==DataType.ENUM && needPlaceholder(c.getOperator())){//数据类型为枚举类型，并且不是包含和是否为空的关系
								newhql.append("? ");
								prameters.add(PermissionUtils.getValueByType(value,c.getEnumPath()));
							}else if(needPlaceholder(c.getOperator())){//条件为非包含 不是是否为空的条件时
								newhql.append("? ");
								prameters.add(PermissionUtils.getValueByType(c.getDataType(), value));
							}else if(containtCondition(c.getOperator())){//条件为包含关系
								newhql.append("? ");
								prameters.add(PermissionUtils.getValueByType(c.getDataType(), "%"+value+"%"));
							}
							if(a<values.length-1){
								newhql.append(analysisLogicOperator(LogicOperator.OR));//value之间是or的关系
							}
							a++;
						}
						newhql.append(")");
					}else{
						if(c.getField().contains("$")){//解析子表字段
							subCon = c.getField().substring(1, c.getField().length());
							//当数据类型是float类型时，需要使用format函数格式化，因为条件为等于或不等于时单精度时查询不准确
							if(c.getDataType()==DataType.FLOAT && (c.getOperator()==FieldOperator.ET || c.getOperator()==FieldOperator.NET))
								subCon = "format("+subCon+",5)";
							newhql.append(subCon);
						}else{
							con = alias+"."+c.getField();
							//当数据类型是float类型时，需要使用format函数格式化，因为条件为等于或不等于时单精度时查询不准确
							if(c.getDataType()==DataType.FLOAT && (c.getOperator()==FieldOperator.ET || c.getOperator()==FieldOperator.NET))
								con = "format("+con+",5)";
							newhql.append(con);
						}
						if(c.getField().contains("$")){
							newhql.append(PermissionUtils.dealTextNullCondition(c.getDataType(), c.getOperator(), subCon));
						}else{
							newhql.append(PermissionUtils.dealTextNullCondition(c.getDataType(), c.getOperator(), con));
						}
						if(c.getDataType()==DataType.ENUM && needPlaceholder(c.getOperator())){//数据类型为枚举类型，并且不是包含和是否为空的关系
							newhql.append("? ");
							prameters.add(PermissionUtils.getValueByType(value,c.getEnumPath()));
						}else if(c.getDataType()==DataType.FLOAT && (c.getOperator()==FieldOperator.ET || c.getOperator()==FieldOperator.NET)){//处理单精度浮点型，条件为等于或不等于时精度问题
							newhql.append("format(?,5) ");
							prameters.add(PermissionUtils.getValueByType(c.getDataType(), value));
						}else if(needPlaceholder(c.getOperator())){//条件为非包含 不是是否为空的条件时
							newhql.append("? ");
							prameters.add(PermissionUtils.getValueByType(c.getDataType(), value));
						}else if(containtCondition(c.getOperator())){//条件为包含关系
							newhql.append("? ");
							prameters.add(PermissionUtils.getValueByType(c.getDataType(), "%"+value+"%"));
						}
					}
					newhql.append(PermissionUtils.getRightBracket(c.getRightBracket()));
					if(i<conditions.size()-1){
						newhql.append(analysisLogicOperator(c.getLgicOperator()));
					}
					i++;
				}
				newhql.append(")");
				if(t<rules.size()-1){
					newhql.append(analysisLogicOperator(link));
				}
				t++;
			}
			
			ConditionResult cr=new ConditionResult();
			if(StringUtils.isNotEmpty(newhql.toString())){
				String condition=" and ("+newhql.toString()+")";
				String where = " where ";
				String order_by = " order by ";
				StringBuilder hqlResult=new StringBuilder();
				if(hql.contains(where) && hql.contains(order_by)){
					String[] arr=hql.split(order_by);
					hqlResult.append(arr[0]);
					hqlResult.append(condition);
					hqlResult.append(order_by);
					hqlResult.append(arr[1]);
				}else if(hql.contains(where)){
					hqlResult.append(hql);
					hqlResult.append(condition);
				}else if(hql.contains(order_by)){
					String[] arr=hql.split(order_by);
					hqlResult.append(arr[0]);
					hqlResult.append(where);
					hqlResult.append(newhql.toString());
					hqlResult.append(order_by);
					hqlResult.append(arr[1]);
				}else{
					hqlResult.append(hql);
					hqlResult.append(where);
					hqlResult.append(newhql.toString());
				}
				cr.setHql(hqlResult.toString());
			}else{
				cr.setHql(hql);
			}
			cr.setPrameters(prameters.toArray());
			return cr;
		}
	private boolean needPlaceholder(FieldOperator fo){
		return !(FieldOperator.CONTAIN==fo || FieldOperator.NOT_CONTAIN==fo 
				|| FieldOperator.IS_NULL==fo || FieldOperator.NOT_NULL==fo);
	}
	private boolean containtCondition(FieldOperator fo){
		return (FieldOperator.CONTAIN==fo || FieldOperator.NOT_CONTAIN==fo );
	}
	
	private String analysisLogicOperator(LogicOperator o){
		if(LogicOperator.AND.equals(o)){
			return " and ";
		}else {
			return " or ";
		}
	}

	public String getSystemUrlByTalbeId(Long dataTableId) {
		DataTable dt = dataTableDao.get(dataTableId);
		Menu menu = menuDao.getMenu(dt.getMenuId());
		return menu.getUrl();
	}

	public String getOptionValue(String dataValue) {
		StringBuilder result = new StringBuilder();
		OptionGroup optionGroup = optionGroupDao.getOptionGroupByCode(dataValue);
		for (Option option : optionGroup.getOptions()) {
			result.append(option.getValue()).append(":").append(option.getName()).append(",");
		}
		return result.toString();
	}

	public void addConditionResult(String hql, List<String> dataRuleCodes,
			LogicOperator link, Object[] values) {
		ConditionResult cr = getConditionResult(hql, dataRuleCodes, link, values);
		Struts2Utils.getRequest().setAttribute(PermissionUtils.PERMISSION_HQL, cr.getHql());
		Struts2Utils.getRequest().setAttribute(PermissionUtils.PERMISSION_PARAMETERS, cr.getPrameters());
	}
}
