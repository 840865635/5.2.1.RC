package com.norteksoft.product.api;

import java.util.List;

import com.norteksoft.mms.authority.enumeration.LogicOperator;

/**
 * 数据授权api
 * @author Administrator
 *
 * @param <T>
 */
public interface DataPermissionService{
	
	/**
	 * 查看权限
	 * @param entity
	 * @return
	 */
	public boolean viewPermission(Object entity);
	
	/**
	 * 根据规则关系获取查看权限
	 * @param entity
	 * @param link
	 * @return
	 */
	public boolean viewPermission(Object entity,LogicOperator link);
	
	/**
	 * 根据规则关系和数据授权编号集合获取查看权限
	 * @param entity
	 * @param link
	 * @param permissionCodes
	 * @return
	 */
	public boolean viewPermission(Object entity,LogicOperator link, List<String> permissionCodes);
	
	/**
	 * 修改权限
	 * @param entity
	 * @return
	 */
	public boolean updatePermission(Object entity);
	
	/**
	 * 根据规则关系获取修改权限
	 * @param entity
	 * @param link
	 * @return
	 */
	public boolean updatePermission(Object entity,LogicOperator link);
	
	/**
	 * 根据规则关系和数据授权编号集合获取修改权限
	 * @param entity
	 * @param link
	 * @param permissionCodes
	 * @return
	 */
	public boolean updatePermission(Object entity,LogicOperator link, List<String> permissionCodes);
	
	/**
	 * 删除权限
	 * @param entity
	 * @return
	 */
	public boolean deletePermission(Object entity);
	
	/**
	 * 根据规则关系获取删除权限
	 * @param entity
	 * @param link
	 * @return
	 */
	public boolean deletePermission(Object entity,LogicOperator link);
	
	/**
	 * 根据规则关系和数据授权编号集合获取删除权限
	 * @param entity
	 * @param link
	 * @param permissionCodes
	 * @return
	 */
	public boolean deletePermission(Object entity,LogicOperator link, List<String> permissionCodes);
	
	
	/**
	 * 添加查询条件
	 * @param entity
	 * @return
	 */
	public <T> void addPermissionCondition(Class<T> className,String hql, Object... values);
	
	/**
	 * 根据规则关系获取查询条件
	 * @param entity
	 * @param link
	 * @return
	 */
	public <T> void addPermissionCondition(Class<T> className,String hql, LogicOperator link, Object... values);
	
	/**
	 * 根据规则关系和数据授权编号集合添加查询条件
	 * @param entity
	 * @param link
	 * @param permissionCodes
	 * @return
	 */
	public <T> void addPermissionCondition(Class<T> className,String hql, LogicOperator link, List<String> permissionCodes,Object... values);
}
