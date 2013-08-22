package com.norteksoft.mms.base.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.proxy.HibernateProxy;

import com.norteksoft.product.api.entity.Role;
import com.norteksoft.product.api.entity.Department;
import com.norteksoft.product.api.entity.Workgroup;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.bs.rank.service.RankManager;
import com.norteksoft.mms.authority.dao.PermissionBaseDao.ConditionResult;
import com.norteksoft.mms.authority.entity.Condition;
import com.norteksoft.mms.authority.entity.DataRule;
import com.norteksoft.mms.authority.entity.PermissionItem;
import com.norteksoft.mms.authority.enumeration.ConditionType;
import com.norteksoft.mms.authority.enumeration.FieldOperator;
import com.norteksoft.mms.authority.enumeration.LeftBracket;
import com.norteksoft.mms.authority.enumeration.RightBracket;
import com.norteksoft.mms.authority.enumeration.UserOperator;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.BeanUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.mms.authority.enumeration.LogicOperator;
import com.norteksoft.wf.base.utils.BeanShellUtil;

/**
 * 根据 PermissionItem 组合,判断当前用户是否满足条件
 * @author xiao
 * 2012-11-2
 */
public class PermissionUtils {
	protected static SessionFactory sessionFactory;
	protected static Log logger = LogFactory.getLog(PermissionUtils.class);
	public static final String PERMISSION_HQL = "permission_hql";
	public static final String PERMISSION_PARAMETERS = "permission_parameters";
	public static final String NO_PERMISSION = "no_permission";
		
	public static Object getLeftBracket(LeftBracket leftBracket) {
		if(leftBracket==null){
			return "";
		}
		switch (leftBracket) {
		case LEFTDOUBLE:
			return " ((";
		case LEFTSINGLE:
			return " (";
		}
		return "";
	}
	public static Object getRightBracket(RightBracket rightBracket) {
		if(rightBracket==null){
			return "";
		}
		switch (rightBracket) {
		case RIGHTDOUBLE:
			return ")) ";
		case RIGHTSINGLE:
			return ") ";
		}
		return "";
	}
	//新建时，如果条件是表单字段，直接返回true，
	//查询时，从对应数据表中查出此字段中的值，和当前用户信息比较，如果符合条件返回true
	//用户比较登录名，部门、角色、工作组比较id
	//如果当前用户直属上级不存在，返回false
	public static boolean itemPermission(PermissionItem item, UserInfo user, boolean fieldFlag, List<Object> values){
		switch (item.getItemType()) {
		case USER:
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.loginNames, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.loginNames, item.getConditionValue());
		case DEPARTMENT:
			if(item.getConditionValue().equals("topDepartment")){//表示顶级部门
				List<Department> depts =  ApiFactory.getAcsService().getDepartments(user.loginName);
				return topDepartmentDecision(user.loginName,depts,item.getOperator());
			}
			user.departments = getDepartments(user.loginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.departments, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.departments, item.getConditionValue());
		case ROLE: 
			user.roles = getRoles(user.loginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.roles, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.roles, item.getConditionValue());
		case WORKGROUP: 
			user.workgroups = getWorkgroups(user.loginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.workgroups, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.workgroups, item.getConditionValue());
		case CURRENT_USER_LOGINNAME:
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.loginNames, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.loginNames, item.getConditionValue());
		case CURRENT_USER_DEPARTMENT:
			if(item.getConditionValue().equals("topDepartment")){//表示顶级部门
				List<Department> depts =  ApiFactory.getAcsService().getDepartments(user.loginName);
				return topDepartmentDecision(user.loginName,depts,item.getOperator());
			}
			user.departments = getDepartments(user.loginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.departments, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.departments, item.getConditionValue());
		case CURRENT_USER_ROLE:
			user.roles = getRoles(user.loginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.roles, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.roles, item.getConditionValue());
		case CURRENT_USER_WORKGROUP:
			user.workgroups = getWorkgroups(user.loginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.workgroups, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.workgroups, item.getConditionValue());
		case CURRENT_USER_SUPERIOR_DEPARTMENT:
			if(item.getConditionValue().equals("topDepartment")){//表示顶级部门
				List<Department> depts = ApiFactory.getAcsService().getParentDepartmentsByUser(user.loginName);
				return topDepartmentDecision(user.loginName,depts,item.getOperator());
			}
			user.departments = getSuperiorDepartments(user.loginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.departments, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.departments, item.getConditionValue());
		case CURRENT_USER_TOP_DEPARTMENT:
			if(item.getConditionValue().equals("topDepartment") && item.getOperator()==UserOperator.ET){//表示顶级部门
				return true;
			}else if(item.getConditionValue().equals("topDepartment") && item.getOperator()==UserOperator.NET){
				return false;
			}
			user.departments = getTopDepartments(user.loginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.departments, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.departments, item.getConditionValue());
		case CURRENT_USER_DIRECT_SUPERIOR_LOGINNAME:
			if(user.directSuperiorLoginName=="")return false;
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.directSuperiorLoginNames, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.directSuperiorLoginNames, item.getConditionValue());
		case CURRENT_USER_DIRECT_SUPERIOR_DEPARTMENT:
			if(user.directSuperiorLoginName=="")return false;
			if(item.getConditionValue().equals("topDepartment")){//表示顶级部门
				List<Department> depts =  ApiFactory.getAcsService().getDepartments(user.directSuperiorLoginName);
				return topDepartmentDecision(user.directSuperiorLoginName,depts,item.getOperator());
			}
			user.departments = getDepartments(user.directSuperiorLoginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.departments, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.departments, item.getConditionValue());
		case CURRENT_USER_DIRECT_SUPERIOR_ROLE:
			if(user.directSuperiorLoginName=="")return false;
			user.roles = getRoles(user.directSuperiorLoginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.roles, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.roles, item.getConditionValue());
		case CURRENT_USER_DIRECT_SUPERIOR_WORKGROUP:
			if(user.directSuperiorLoginName=="")return false;
			user.workgroups = getWorkgroups(user.directSuperiorLoginName);
			if(fieldFlag){
				for (Object o : values) {
					if(o==null) return false;
					if(permissionDecision(item.getOperator(), user.workgroups, o.toString())){
						return true;
					}
				}
				return false;
			}
			return permissionDecision(item.getOperator(), user.workgroups, item.getConditionValue());
		}
		return false;
	}
	
	private static boolean topDepartmentDecision(String  loginName,List<Department> depts, UserOperator userOperator) {
		boolean inTopDepartment = false;
		List<Department> topDepts = ApiFactory.getAcsService().getTopDepartmentsByUser(loginName);
		for (Department department : topDepts) {
			for (Department dep : depts) {
				if(dep.getId()==department.getId()){
					inTopDepartment =  true; break;
				}
			}
		}
		if(inTopDepartment && userOperator==UserOperator.ET){//在顶级部门，并且关系为等于
			return true;
		}else if(!inTopDepartment && userOperator==UserOperator.NET){//不在顶级部门，关系为不等于
			return true;
		}else{
			return false;
		}
	}

	private static boolean permissionDecision(UserOperator operator, List<String> src, String value){
		switch (operator) {
		case ET:
			if(src.contains(value)) return true;
			break;
		case NET: 
			if(!src.contains(value)) return true;
			break;
		}
		return false;
	}
	
	public static String joinType(LogicOperator type){
		switch (type) {
		case AND:
			return " &&";
		case OR:
			return " ||";
		}
		return "";
	}
	
	private static List<String> getRoles(String loginName){
		Set<Role> roles = ApiFactory.getAcsService().getRolesByUser(loginName);
		List<String> result = new ArrayList<String>();
		for(Role role : roles){
			result.add(role.getId().toString());
		}
		return result;
	}
	
	private static List<String> getWorkgroups(String loginName){
		List<Workgroup> groups = ApiFactory.getAcsService().getWorkgroupsByUser(loginName);
		List<String> result = new ArrayList<String>();
		for(Workgroup group : groups){
			result.add(group.getId().toString());
		}
		return result;
	}
	
	private static List<String> getDepartments(String loginName){
		List<Department> depts = ApiFactory.getAcsService().getDepartments(loginName);
		List<String> result = new ArrayList<String>();
		for(Department dept : depts){
			result.add(dept.getId().toString());
		}
		return result;
	}
	private static List<String> getSuperiorDepartments(String loginName){
		List<Department> depts = ApiFactory.getAcsService().getParentDepartmentsByUser(loginName);
		List<String> result = new ArrayList<String>();
		for(Department dept : depts){
			result.add(dept.getId().toString());
		}
		return result;
	}
	private static List<String> getTopDepartments(String loginName){
		List<Department> depts = ApiFactory.getAcsService().getTopDepartmentsByUser(loginName);
		List<String> result = new ArrayList<String>();
		for(Department dept : depts){
			result.add(dept.getId().toString());
		}
		return result;
	}
	
	public static class UserInfo{
		String loginName;
		String directSuperiorLoginName;
		List<String> loginNames = new ArrayList<String>();
		List<String> directSuperiorLoginNames = new ArrayList<String>();
		List<String> roles;
		List<String> departments;
		List<String> workgroups;
		
		public UserInfo(String loginName,String directSuperiorLoginName){
			this.loginName = loginName;
			this.loginNames.add(loginName);
			this.directSuperiorLoginName = directSuperiorLoginName;
			this.directSuperiorLoginNames.add(directSuperiorLoginName);
		}
	}
	
	/**
	 * 实体是否满足数据规则
	 * @param entity
	 * @param link 
	 * @param rule
	 */
	public static boolean entityPermission(Object entity, List<DataRule> dataRules, LogicOperator link){
		StringBuilder sb = new StringBuilder();
		Object obj = null;
		Condition con =null;
		try {
			for (DataRule dataRule : dataRules) {
				List<Condition> conditions = dataRule.getConditions();
				sb.append("(");
				for(int i=0;i<conditions.size();i++){
					con = conditions.get(i);
					String value = con.getConditionValue();
					if(entity instanceof HibernateProxy){
						HibernateProxy proxy = (HibernateProxy)entity;
						entity = proxy.getHibernateLazyInitializer().getImplementation();
					}
					if(con.getRelativeType()!=null){//解析相对条件
						value = PermissionUtils.getValueByRelativeType(con.getRelativeType());
						String[] values =  value.split(",");
						sb.append(getLeftBracket(con.getLeftBracket()));
						sb.append("(");
						int valIndex=0;
						for (String val : values) {
 							sb.append(getRelativeExpressByCondition(con,entity,val));
							if(valIndex<values.length-1){
								sb.append(joinType(LogicOperator.OR));
							}
							valIndex++;
						}
						sb.append(")");
						sb.append(getRightBracket(con.getRightBracket()));
					}else{
						sb.append(getExpressByCondition(con,entity,value));
					}
					if(i<conditions.size()-1){//表示最后一个不加连接符
						sb.append(joinType(con.getLgicOperator()));
					}
				}
				sb.append(")").append(joinType(link));
			}
		} catch (Exception e) {
			logger.error("Compare value error. Field:[" + con.getField() + 
					"], SRC: [" + obj + "], DEST:["+con.getConditionValue()+"]", e);
		}
		String express = sb.substring(0, sb.length()-2);
		return BeanShellUtil.evel(express);
	}
	
	private static String getExpressByCondition(Condition con,Object entity, String value) throws Exception {
		StringBuilder sb = new StringBuilder();
		Object obj = null;
		boolean result;
		if(con.getField().contains("$")){//表示子表字段（子表和主表是一对多的关系）
			String[] field = con.getField().substring(1,con.getField().length()).split("\\.");
			obj = BeanUtils.getFieldValue(entity, field[0]);
			List<Object> o =  (List<Object>)obj;
			if(o.size()>0){
				sb.append(getLeftBracket(con.getLeftBracket()));
				sb.append("(");
				for(int j=0;j<o.size();j++){
					obj = BeanUtils.getFieldValue(o.get(j), field[1]);
					result =calculateCondition(obj, con.getOperator(), con.getDataType(),value,con.getEnumPath());
					sb.append(result);
					if(j<o.size()-1){
						sb.append(joinType(LogicOperator.OR));
					}
				}
				sb.append(getRightBracket(con.getRightBracket()));
				sb.append(")");
			}
		}else{
			obj = BeanUtils.getFieldValue(entity, con.getField());
			result = calculateCondition(obj, con.getOperator(), con.getDataType(), value,con.getEnumPath());
			sb.append(getLeftBracket(con.getLeftBracket())).append(result).append(getRightBracket(con.getRightBracket()));
		}
		return sb.toString();
	}
	
	
	private static String getRelativeExpressByCondition(Condition con,Object entity, String value) throws Exception {
		StringBuilder sb = new StringBuilder();
		Object obj = null;
		boolean result;
		if(con.getField().contains("$")){//表示子表字段（子表和主表是一对多的关系）
			String[] field = con.getField().substring(1,con.getField().length()).split("\\.");
			obj = BeanUtils.getFieldValue(entity, field[0]);
			List<Object> o =  (List<Object>)obj;
			if(o.size()>0){
				sb.append("(");
				for(int j=0;j<o.size();j++){
					obj = BeanUtils.getFieldValue(o.get(j), field[1]);
					result =calculateCondition(obj, con.getOperator(), con.getDataType(),value,con.getEnumPath());
					sb.append(result);
					if(j<o.size()-1){
						sb.append(joinType(LogicOperator.OR));
					}
				}
				sb.append(")");
			}
		}else{
			obj = BeanUtils.getFieldValue(entity, con.getField());
			result = calculateCondition(obj, con.getOperator(), con.getDataType(), value,con.getEnumPath());
			sb.append(result);
		}
		return sb.toString();
	}
	/**
	 * 计算表达式的值
	 * @param obj  原始值
	 * @param fo   比较符
	 * @param dt   数据类型
	 * @param value  比较的值
	 * @return
	 */
	public static boolean calculateCondition(Object obj, FieldOperator fo, DataType dt, String value,String enumPath){
		if(fo==FieldOperator.IS_NULL || fo== FieldOperator.NOT_NULL){
			if(obj!=null && StringUtils.isEmpty(obj.toString())) obj = null;
		}
		switch (fo) {
		case IS_NULL: return obj == null;
		case NOT_NULL: return obj != null;
		case CONTAIN: 
			if(dt==DataType.TEXT && obj!=null && value!=null){
				return obj.toString().contains(value);
			}
			return false;
		case NOT_CONTAIN: 
			if(dt==DataType.TEXT && obj!=null && value!=null){
				return !obj.toString().contains(value);
			}
			return false;
		case ET: 
			if(obj!=null && value!=null){
				if(dt==DataType.ENUM){//处理枚举类型
					return obj.equals(getValueByType(value, enumPath));
				}
				if(dt==DataType.DATE){//处理日期类型
					return ((Date)obj).getTime()==((Date)getValueByType(dt, value)).getTime();
				}
				return obj.equals(getValueByType(dt, value));
			}
			return false;
		case NET: 
			if(obj!=null && value!=null){
				if(dt==DataType.ENUM){//处理枚举类型
					return !obj.equals(getValueByType(value, enumPath));
				}
				if(dt==DataType.DATE){//处理日期类型
					return !(((Date)obj).getTime()==((Date)getValueByType(dt, value)).getTime());
				}
				return !obj.equals(getValueByType(dt, value));
			}
			return false;
		case GT: 
			if(comparableBigSmall(dt)){
				return CompareUtils.compareGT(dt, obj, getValueByType(dt, value));
			}
			return false;
		case GET: 
			if(comparableBigSmall(dt)){
				return CompareUtils.compareGET(dt, obj, getValueByType(dt, value));
			}
			return false;
		case LT: 
			if(comparableBigSmall(dt)){
				return CompareUtils.compareLT(dt, obj, getValueByType(dt, value));
			}
			return false;
		case LET: 
			if(comparableBigSmall(dt)){
				return CompareUtils.compareLET(dt, obj, getValueByType(dt, value));
			}
			return false;
		}
		return false;
	}
	
	private static boolean comparableBigSmall(DataType dt){
		return DataType.DATE==dt||DataType.TIME==dt||DataType.INTEGER==dt
				||DataType.LONG==dt||DataType.DOUBLE==dt||DataType.FLOAT==dt;
	}
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public static Object getValueByType(DataType dt, String value){
		if(StringUtils.isEmpty(value)&& dt!=DataType .TEXT ) return null;
		try {
			switch (dt) {
			case TEXT: return value;
			case DATE: return DATE_FORMAT.parse(value);
			case TIME: return TIME_FORMAT.parse(value);
			case INTEGER: return Integer.valueOf(value);
			case LONG: return Long.valueOf(value);
			case DOUBLE: return Double.valueOf(value);
			case FLOAT: return Float.valueOf(value);
			case BOOLEAN: return Boolean.valueOf(value);
			case ENUM: break;
			}
		} catch (Exception e) {
			logger.error("Parse string to " + dt + " error. string["+value+"]", e);
		}
		return null;
	}
	
	public static Object getValueByType( String value,String enumPath){
		if(StringUtils.isEmpty(value)) return null;
		try {
			Object[] enumValues = Class.forName(enumPath).getEnumConstants();
			for (Object object : enumValues) {
				if(object.toString().equals(value)){
					return object;
				}
			}
		} catch (Exception e) {
			logger.error("Parse  " + value + " to enum:"+enumPath+"error.", e);
		}
		return null;
	}
	/**
	 * 原生SQL查询
	 * @param sql
	 * @param values
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	private static List<Object> findBySql(String sql, Object... values){
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		if(values != null){
			for(int i = 0; i < values.length; i++){
				sqlQuery.setParameter(i, values[i]);
			}
		}
		return sqlQuery.list();
	}
	
	/**
	 * 取得当前Session.
	 */
	private static Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	
	public static String getValueByRelativeType(ConditionType relativeType) {
		String value = "";
		String loginName = ContextUtils.getLoginName();
		switch (relativeType) {
		case CURRENT_USER_LOGINNAME:
			value =  loginName; 
			break;
		case CURRENT_USER_DEPARTMENT:
			 List<Department> depts = ApiFactory.getAcsService().getDepartments(loginName);
			 for (Department department : depts) {
				value = value+ department.getName()+",";
			}
			break;
		case CURRENT_USER_ROLE:
			Set<Role> roles = ApiFactory.getAcsService().getRolesByUser(loginName);
			for (Role role : roles) {
				value = value+ role.getId()+",";
			}
			break;
		case CURRENT_USER_WORKGROUP:
			 List<Workgroup> workgroups = ApiFactory.getAcsService().getWorkgroupsByUser(loginName);
			 for (Workgroup workgroup : workgroups) {
				 value = value+ workgroup.getName()+",";
			}
			 break;
		case CURRENT_USER_SUPERIOR_DEPARTMENT:
			 List<Department> parentDepts = ApiFactory.getAcsService().getParentDepartmentsByUser(loginName);
			 for (Department department : parentDepts) {
				 value = value+ department.getName()+",";
			}
			 break;
		case CURRENT_USER_TOP_DEPARTMENT:
			List<Department> topDepts = ApiFactory.getAcsService().getTopDepartmentsByUser(loginName);
			for (Department department : topDepts) {
				 value = value+ department.getName()+",";
			}
			break;
		case CURRENT_USER_DIRECT_SUPERIOR_LOGINNAME:
			value =  getDirectLeader();
			break;
		case CURRENT_USER_DIRECT_SUPERIOR_DEPARTMENT:
			if(StringUtils.isEmpty(getDirectLeader())) return "";
			 List<Department> superiorDepts = ApiFactory.getAcsService().getDepartments(getDirectLeader());
			 for (Department department : superiorDepts) {
				value = value+ department.getName()+",";
			}
			 break;
		case CURRENT_USER_DIRECT_SUPERIOR_ROLE:
			if(StringUtils.isEmpty(getDirectLeader())) return "";
			Set<Role> superiorRoles = ApiFactory.getAcsService().getRolesByUser(getDirectLeader());
			for (Role role : superiorRoles) {
				value = value+ role.getId()+",";
			}
			break;
		case CURRENT_USER_DIRECT_SUPERIOR_WORKGROUP:
			if(StringUtils.isEmpty(getDirectLeader())) return "";
			 List<Workgroup> superiorWorkgroups = ApiFactory.getAcsService().getWorkgroupsByUser(getDirectLeader());
			 for (Workgroup workgroup : superiorWorkgroups) {
				 value = value+ workgroup.getName()+",";
			}
			 break;
		}
		return value;
	}
	
	//获取直属上级
	public static String getDirectLeader() {
		RankManager rankManager=(RankManager)ContextUtils.getBean("rankManager");
		User directLeader = rankManager.getDirectLeader(ContextUtils.getLoginName());
		if(directLeader!=null){
			return directLeader.getLoginName();
		}
		return "";
	}
	
	public static String dealTextNullCondition(DataType dataType,FieldOperator operator,String con){
		StringBuilder sb = new StringBuilder();
		if(dataType==DataType.TEXT &&operator==FieldOperator.IS_NULL){
			//文本类型，如果规则条件为空，则条件为 : a is null or a ='' 
			sb.append(operator.sign).append(" or ").append(con).append("='' ");
		}else if(dataType==DataType.TEXT &&operator==FieldOperator.NOT_NULL){
			//文本类型，如果规则条件不为空，则条件为 : a is not null and a<>'' 
			sb.append(operator.sign).append(" and ").append(con).append("<>'' ");
		}else{
			sb.append(operator.sign);
		}
		return sb.toString();
	}
	
	public static List<Object> getPermissionResult(String hql,Object[] values) {
		List<Object> obj = new ArrayList<Object>(); 
		String permissionHql = (String)Struts2Utils.getRequest().getAttribute(PermissionUtils.PERMISSION_HQL);
		Object[] parameter = (Object[])Struts2Utils.getRequest().getAttribute(PermissionUtils.PERMISSION_PARAMETERS);
		if(permissionHql==null){
			obj.add(hql);
			obj.add(values);
		}else if(permissionHql.equals(PermissionUtils.NO_PERMISSION)){
			obj.add(PermissionUtils.NO_PERMISSION);
			obj.add(parameter);
		}else{
			obj.add(permissionHql);
			obj.add(parameter);
		}
		return obj;
	}
}
