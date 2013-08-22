package com.norteksoft.mms.authority.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.norteksoft.mms.authority.entity.Condition;
import com.norteksoft.mms.authority.entity.DataRule;
import com.norteksoft.mms.authority.entity.Permission;
import com.norteksoft.mms.authority.entity.PermissionItem;
import com.norteksoft.mms.authority.enumeration.FieldOperator;
import com.norteksoft.mms.authority.enumeration.LogicOperator;
import com.norteksoft.mms.authority.enumeration.PermissionAuthorize;
import com.norteksoft.mms.base.utils.PermissionUtils;
import com.norteksoft.mms.base.utils.PermissionUtils.UserInfo;
import com.norteksoft.mms.form.dao.DataTableDao;
import com.norteksoft.mms.form.entity.DataTable;
import com.norteksoft.mms.form.entity.TableColumn;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.mms.form.service.TableColumnManager;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.wf.base.utils.BeanShellUtil;
@Repository
public class PermissionBaseDao<T, PK extends Serializable> extends HibernateDao<T, PK>{
	private final String NO_VALUE_STRING= "NO_VALUE_#~_+~=%";
	@Autowired
	private DataTableDao dataTableDao;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private TableColumnManager tableColumnManager;
	
	public <T> void addPermissionCondition(Class<T> className,String hql, Object... values){
		addConditionResult(className,hql,LogicOperator.OR,null,values);
	}
	
	public <T> void addPermissionCondition(Class<T> className,String hql, LogicOperator link, Object... values){
		addConditionResult(className,hql,link,null,values);
	}
	
	public <T> void addPermissionCondition(Class<T> className,String hql, LogicOperator link, List<String> permissionCodes, Object... values){
		addConditionResult(className,hql,link,permissionCodes,values);
	}
	
	public <T> void addConditionResult(Class<T> className,String hql,LogicOperator link,List<String> permissionCodes, Object... values){
		AuthorityResult  ar = getSearchAuthorityDataRule(className,PermissionAuthorize.SEARCH);
		if(!ar.result){//表示没有授权，可以查看所有数据
			Struts2Utils.getRequest().setAttribute(PermissionUtils.PERMISSION_HQL, null);
			Struts2Utils.getRequest().setAttribute(PermissionUtils.PERMISSION_PARAMETERS, null);
		}else if(ar.dataRules.size()<1){//授权了，没有权限
			Struts2Utils.getRequest().setAttribute(PermissionUtils.PERMISSION_HQL,PermissionUtils.NO_PERMISSION);
			Struts2Utils.getRequest().setAttribute(PermissionUtils.PERMISSION_PARAMETERS, null);
		}else{
			ConditionResult cr = getPermissionHqlPamateters(hql, ar.dataRules,link, values);
			Struts2Utils.getRequest().setAttribute(PermissionUtils.PERMISSION_HQL, cr.getHql());
			Struts2Utils.getRequest().setAttribute(PermissionUtils.PERMISSION_PARAMETERS, cr.getPrameters());
		}
	}
	/**
	 * 查询	
	 * @param <T>
	 * @param className
	 * @param authority
	 * @return
	 */
	private  <T> AuthorityResult getSearchAuthorityDataRule(Class<T> className,PermissionAuthorize authority) {
		DataTable table = dataTableDao.getDataTableByEntity(className.getName());
		List<Permission> ps = permissionDao.getPermissionsByDataTableId(table.getId());
		List<DataRule> rules =getDataRuleByPermission(ps,authority,table,null);
		//如果没有授权，增删查改不受授权限制
		if(ps.isEmpty() && rules.size()<1) return new AuthorityResult(rules, false);
		return new AuthorityResult(rules, true);
	}

	/**
	 * 查看权限
	 * @param entity
	 */
	public boolean viewPermission(T entity){
		try {
			AuthorityResult ar = getAuthorityDataRule(PermissionAuthorize.SEARCH,entity);
			if(!ar.result) return true;
			if(ar.dataRules.size()<1) return false;
			return PermissionUtils.entityPermission(entity, ar.dataRules,LogicOperator.OR);
		} catch (Exception e) {
			logger.error("Get update permission error. ", e);
		}
		return false;
	}
	
	/**
	 * 查看权限
	 * @param entity
	 */
	public boolean viewPermission(T entity,LogicOperator link){
		try {
			AuthorityResult ar = getAuthorityDataRule(PermissionAuthorize.SEARCH,entity);
			if(!ar.result) return true;
			if(ar.dataRules.size()<1) return false;
			return PermissionUtils.entityPermission(entity, ar.dataRules,link);
		} catch (Exception e) {
			logger.error("Get update permission error. ", e);
		}
		return false;
	}
	
	/**
	 * 查看权限
	 * @param entity
	 */
	public boolean viewPermission(T entity,LogicOperator link,List<String> permissionCodes){
		try {
			AuthorityResult ar = getAuthorityDataRule(PermissionAuthorize.SEARCH,permissionCodes,entity);
			if(!ar.result) return true;
			if(ar.dataRules.size()<1) return false;
			return PermissionUtils.entityPermission(entity, ar.dataRules,link);
		} catch (Exception e) {
			logger.error("Get update permission error. ", e);
		}
		return false;
	}
	
	/**
	 * 修改权限
	 * @param entity
	 */
	public boolean updatePermission(T entity){
		try {
			AuthorityResult ar = getAuthorityDataRule(PermissionAuthorize.UPDATE,entity);
			if(!ar.result) return true;
			if(ar.dataRules.size()<1) return false;
			return PermissionUtils.entityPermission(entity, ar.dataRules,LogicOperator.OR);
		} catch (Exception e) {
			logger.error("Get update permission error. ", e);
		}
		return false;
	}
	
	/**
	 * 修改权限
	 * @param entity
	 */
	public boolean updatePermission(T entity,LogicOperator link){
		try {
			AuthorityResult ar = getAuthorityDataRule(PermissionAuthorize.UPDATE,entity);
			if(!ar.result) return true;
			if(ar.dataRules.size()<1) return false;
			return PermissionUtils.entityPermission(entity, ar.dataRules,link);
		} catch (Exception e) {
			logger.error("Get update permission error. ", e);
		}
		return false;
	}
	
	/**
	 * 修改权限
	 * @param entity
	 */
	public boolean updatePermission(T entity,LogicOperator link,List<String> permissionCodes){
		try {
			AuthorityResult ar = getAuthorityDataRule(PermissionAuthorize.UPDATE,permissionCodes,entity);
			if(!ar.result) return true;
			if(ar.dataRules.size()<1) return false;
			return PermissionUtils.entityPermission(entity, ar.dataRules,link);
		} catch (Exception e) {
			logger.error("Get update permission error. ", e);
		}
		return false;
	}
	
	/**
	 * 删除权限
	 * @param entity
	 */
	public boolean deletePermission(T entity){
		try {
			AuthorityResult ar = getAuthorityDataRule(PermissionAuthorize.DELETE,entity);
			if(!ar.result) return true;
			if(ar.dataRules.size()<1) return false;
			return PermissionUtils.entityPermission(entity, ar.dataRules,LogicOperator.OR);
		} catch (Exception e) {
			logger.error("Get update permission error. ", e);
		}
		return false;
	}
	
	/**
	 * 删除权限
	 * @param entity
	 */
	public boolean deletePermission(T entity,LogicOperator link){
		try {
			AuthorityResult ar = getAuthorityDataRule(PermissionAuthorize.DELETE);
			if(!ar.result) return true;
			if(ar.dataRules.size()<1) return false;
			return PermissionUtils.entityPermission(entity, ar.dataRules,link);
		} catch (Exception e) {
			logger.error("Get update permission error. ", e);
		}
		return false;
	}
	
	/**
	 * 删除权限
	 * @param entity
	 */
	public boolean deletePermission(T entity,LogicOperator link, List<String> permissionCodes){
		try {
			AuthorityResult ar = getAuthorityDataRule(PermissionAuthorize.DELETE,permissionCodes,entity);
			if(!ar.result) return true;
			if(ar.dataRules.size()<1) return false;
			return PermissionUtils.entityPermission(entity, ar.dataRules,link);
		} catch (Exception e) {
			logger.error("Get update permission error. ", e);
		}
		return false;
	}
	
	/**
	 * 根据操作查询有权限的规则
	 * @param authority 查询
	 * @return
	 */
	public AuthorityResult getAuthorityDataRule(PermissionAuthorize authority){
		DataTable table = dataTableDao.getDataTableByEntity(entityClass.getName());
		List<Permission> ps = permissionDao.getPermissionsByDataTableId(table.getId());
		List<DataRule> rules =getDataRuleByPermission(ps,authority,table,null);
		//如果没有授权，增删查改不受授权限制
		if(ps.isEmpty() && rules.size()<1) return new AuthorityResult(rules, false);
		return new AuthorityResult(rules, true);
	}
	
	/**
	 * 根据操作查询有权限的规则
	 * @param authority 保存,修改,删除
	 * @return
	 */
	protected AuthorityResult getAuthorityDataRule(PermissionAuthorize authority,T entity){
		String className = entity.getClass().getName();
		if(className.contains("_")){
			className =className.substring(0,className.indexOf("_"));
		}
		DataTable table = dataTableDao.getDataTableByEntity(className);
		List<Permission> ps = permissionDao.getPermissionsByDataTableId(table.getId());
		List<DataRule> rules = getDataRuleByPermission(ps,authority,table,entity);
		//如果没有授权，增删查改不受授权限制
		if(ps.isEmpty() && rules.size()<1) return new AuthorityResult(rules, false);
		return new AuthorityResult(rules, true);
	}
	
	/**
	 * 根据操作查询有权限的规则
	 * @param authority 查询, 修改, 删除
	 * @return
	 */
	public AuthorityResult getAuthorityDataRule(PermissionAuthorize authority,List<String> permissionCodes,T entity){
		DataTable table = null;
		if(permissionCodes.size()>0){
			table = dataTableDao.get(permissionDao.getPermissionsByCode(permissionCodes.get(0)).getDataRule().getDataTableId());
		}
		List<Permission> ps = new ArrayList<Permission>();
		for (String permissionCode : permissionCodes) {
			ps.add(permissionDao.getPermissionsByCode(permissionCode));
		}
		List<DataRule> rules = getDataRuleByPermission(ps,authority,table,entity);
		//如果没有授权，增删查改不受授权限制
		if(ps.isEmpty() && rules.size()<1) return new AuthorityResult(rules, false);
		return new AuthorityResult(rules, true);
	}
	
	/**
	 * 根据数据授权，获取数据类别
	 * @param ps
	 * @param authority
	 * @param table
	 * @return
	 */
	private List<DataRule> getDataRuleByPermission(List<Permission> ps,PermissionAuthorize authority,DataTable table,T entity) {
		List<DataRule> rules = new ArrayList<DataRule>();
		UserInfo user = new UserInfo(ContextUtils.getLoginName(),PermissionUtils.getDirectLeader());
		for(Permission p : ps){
			if((p.getAuthority() & authority.getCode()) == 0) continue;
			boolean haPermission = hasPermission(p.getItems(), user,table,authority,entity);
			if(haPermission){
				rules.add(p.getDataRule());
			}
		}
		return rules;
	}
	
	//entity为空时表示查询，不为空时保存和删除
	public boolean hasPermission(List<PermissionItem> items, UserInfo user, DataTable dataTable, PermissionAuthorize authority,T entity){
		StringBuilder result = new StringBuilder();
		for(PermissionItem item : items){
			String sql = "";
			List<Object> values = new ArrayList<Object>();
			boolean fieldFlag = false;//从表单中选择字段的标志
			if(item.getConditionValue().contains("fieldValue-")){
				fieldFlag = true;
				String fieldName = item.getConditionValue().split("-")[1];
				if(authority==PermissionAuthorize.SEARCH){//查询时，需从数据库表中查询此字段的所有值
					String dbName = fieldName;
					if(StringUtils.isNotEmpty(dataTable.getEntityName())){//表示标准表单
						TableColumn column=tableColumnManager.getTableColumnByColName(dataTable.getId(), fieldName);
						if(column!=null){
							dbName=column.getDbColumnName();//获取数据库字段名
						}
					}
					sql = "select distinct t."+dbName+" from "+dataTable.getName()+" t  where t."+dbName+" is not null";
					values = this.findBySql(sql);
				}else{//保存、更新时
					try {
						values.add(BeanUtils.getProperty(entity, fieldName));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}
			result.append(" ").append(PermissionUtils.getLeftBracket(item.getLeftBracket())).append(PermissionUtils.itemPermission(item, user,fieldFlag,values)).append(PermissionUtils.getRightBracket(item.getRightBracket())).append(PermissionUtils.joinType(item.getJoinType()));
		}
		String express = result.substring(0, result.length()-2);
		return BeanShellUtil.evel(express);
	}
	
	
	/**
	 * 根据HQL语句和条件集合拼接HQL，并重新组装条件
	 * @param hql  HQL 如： select x form XX x where x.p=? order by x.op
	 * @param conditions  集合
	 * @param prmts  HQL参数列表
	 * @return 
	 */
	public ConditionResult getPermissionHqlPamateters(String hql, List<DataRule> dataRules,LogicOperator operator,Object... prmts){
		String alias = getAlias(hql); //HQL实体别名 
		StringBuilder newhql=new StringBuilder();
		List<Object> prameters=new ArrayList<Object>();
		for(Object o:prmts){
			prameters.add(o);
		}
		int t = 0;
		for (DataRule rule : dataRules) {
			if(rule==null) {
				t++; continue;
			}
			int i = 0;
			List<Condition> conditions  = rule.getConditions();
			newhql.append("(");
			for(Condition c:conditions){
				newhql.append(PermissionUtils.getLeftBracket(c.getLeftBracket()));
				String value = c.getConditionValue();
				String con = "",subCon = "";
				//如果是相对条件，解析相对条件，相对条件不可能有float类型
				if(c.getRelativeType()!=null){
					value = PermissionUtils.getValueByRelativeType(c.getRelativeType());
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
						//处理字段为空不为空的情况
						if(c.getField().contains("$")){
							newhql.append(PermissionUtils.dealTextNullCondition(c.getDataType(), c.getOperator(), subCon));
						}else{
							newhql.append(PermissionUtils.dealTextNullCondition(c.getDataType(), c.getOperator(), con));
						}
						
						if(c.getDataType()==DataType.ENUM && needPlaceholder(c.getOperator())){//数据类型为枚举类型，条件不是包含关系，也不是为空不为空的关系
							newhql.append("? ");
							prameters.add(PermissionUtils.getValueByType(value,c.getEnumPath()));
						}else if(needPlaceholder(c.getOperator())){//条件不是包含关系，也不是为空不为空的关系
							newhql.append("? ");
							prameters.add(PermissionUtils.getValueByType(c.getDataType(), value));
						}else if(containtCondition(c.getOperator())){//条件为包含关系
							if(StringUtils.isEmpty(value)) value = NO_VALUE_STRING;
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
						if(c.getDataType()==DataType.FLOAT && (c.getOperator()==FieldOperator.ET || c.getOperator()==FieldOperator.NET ))
							subCon = "format("+subCon+",5)";
						newhql.append(subCon);
					}else{
						con = alias+"."+c.getField();
						//当数据类型是float类型时，需要使用format函数格式化，因为条件为等于或不等于时单精度时查询不准确
						if(c.getDataType()==DataType.FLOAT && (c.getOperator()==FieldOperator.ET || c.getOperator()==FieldOperator.NET))
							con = "format("+con+",5)";
						newhql.append(con);
					}
					//处理字段为空不为空的情况
					if(c.getField().contains("$")){
						newhql.append(PermissionUtils.dealTextNullCondition(c.getDataType(), c.getOperator(), subCon));
					}else{
						newhql.append(PermissionUtils.dealTextNullCondition(c.getDataType(), c.getOperator(), con));
					}
					if(c.getDataType()==DataType.ENUM && needPlaceholder(c.getOperator())){//数据类型为枚举类型，条件不是包含关系，也不是为空不为空的关系
						newhql.append("? ");
						prameters.add(PermissionUtils.getValueByType(value,c.getEnumPath()));
					}else if(c.getDataType()==DataType.FLOAT && (c.getOperator()==FieldOperator.ET || c.getOperator()==FieldOperator.NET)){//处理单精度浮点型，条件为等于或不等于时精度问题
						newhql.append("format(?,5) ");
						prameters.add(PermissionUtils.getValueByType(c.getDataType(), value));
					}else if(needPlaceholder(c.getOperator())){//条件不是包含关系，也不是为空不为空的关系
						newhql.append("? ");
						prameters.add(PermissionUtils.getValueByType(c.getDataType(), value));
					}else if(containtCondition(c.getOperator())){//条件为包含关系
						if(StringUtils.isEmpty(value)) value = NO_VALUE_STRING;
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
			if(t<dataRules.size()-1){
				newhql.append(analysisLogicOperator(operator));
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

	private static boolean needPlaceholder(FieldOperator fo){
		return !(FieldOperator.CONTAIN==fo || FieldOperator.NOT_CONTAIN==fo 
				|| FieldOperator.IS_NULL==fo || FieldOperator.NOT_NULL==fo);
	}
	private static boolean containtCondition(FieldOperator fo){
		return (FieldOperator.CONTAIN==fo || FieldOperator.NOT_CONTAIN==fo );
	}
	
	private static String analysisLogicOperator(LogicOperator o){
		if(LogicOperator.AND.equals(o)){
			return " and ";
		}else {
			return " or ";
		}
	}
	
	static class AuthorityResult{
		List<DataRule> dataRules;
		boolean result;
		public AuthorityResult(List<DataRule> dataRules, boolean result) {
			this.dataRules = dataRules;
			this.result = result;
		}
		public AuthorityResult(){}
	}
	
	public static class ConditionResult{
		private String hql;
		private Object[] prameters;
		public String getHql() {
			return hql;
		}
		public void setHql(String hql) {
			this.hql = hql;
		}
		public Object[] getPrameters() {
			return prameters;
		}
		public void setPrameters(Object[] prameters) {
			this.prameters = prameters;
		}
	}
}
