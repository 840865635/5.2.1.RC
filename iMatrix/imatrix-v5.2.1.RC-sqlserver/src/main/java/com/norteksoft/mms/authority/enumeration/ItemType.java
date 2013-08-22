package com.norteksoft.mms.authority.enumeration;

public enum ItemType {
	/**
	 * 人员
	 */
	USER("permission.item.type.user"),
	/**
	 * 部门
	 */
	DEPARTMENT("permission.item.type.department"),
	/**
	 * 角色
	 */
	ROLE("permission.item.type.role"),
	/**
	 * 工作组
	 */
	WORKGROUP("permission.item.type.workgroup"),
	
	/**
	 * 当前用户登录名
	 */
	CURRENT_USER_LOGINNAME("permission.item.type.currentUserLoginName"),
	
	/**
	 * 当前用户部门
	 */
	CURRENT_USER_DEPARTMENT("permission.item.type.currentUserDepartment"),
	
	/**
	 * 当前用户角色
	 */
	CURRENT_USER_ROLE("permission.item.type.currentUserRole"),
	
	/**
	 * 当前用户工作组
	 */
	CURRENT_USER_WORKGROUP("permission.item.type.currentUserWorkgroup"),
	
	/**
	 * 当前用户上级部门
	 */
	CURRENT_USER_SUPERIOR_DEPARTMENT("permission.item.type.currentUserSuperiorDepartment"),
	
	/**
	 * 当前用户顶级部门
	 */
	CURRENT_USER_TOP_DEPARTMENT("permission.item.type.currentUserTopDepartment"),
	
	/**
	 * 直属上级登录名
	 */
	CURRENT_USER_DIRECT_SUPERIOR_LOGINNAME("permission.item.type.currentUserDirectSuperiorLoginName"),
	/**
	 * 直属上级部门
	 */
	CURRENT_USER_DIRECT_SUPERIOR_DEPARTMENT("permission.item.type.currentUserDirectSuperiorDepartment"),
	/**
	 * 直属上级角色
	 */
	CURRENT_USER_DIRECT_SUPERIOR_ROLE("permission.item.type.currentUserDirectSuperiorRole"),
	/**
	 * 直属上级工作组
	 */
	CURRENT_USER_DIRECT_SUPERIOR_WORKGROUP("permission.item.type.currentUserDirectSuperiorWorkgroup");
	public String code;
	ItemType(String code){
		this.code=code;
	}
	public Short getIndex(){
		return (short)(this.ordinal()+1);
	}
	public String getCode(){
		return this.code;
	}
}
