package com.norteksoft.product.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.norteksoft.mms.authority.dao.PermissionExtendBaseDao;
import com.norteksoft.mms.authority.enumeration.LogicOperator;
import com.norteksoft.product.api.DataPermissionService;

@Service
@Transactional
public class DataPermissionServiceImpl implements DataPermissionService {
	@Autowired
	private PermissionExtendBaseDao permissionExtendBaseDao;
	

	public boolean deletePermission(Object entity) {
		return permissionExtendBaseDao.deletePermission(entity);
	}

	public boolean deletePermission(Object entity, LogicOperator link) {
		return permissionExtendBaseDao.deletePermission(entity, link);
	}

	public boolean deletePermission(Object entity, LogicOperator link,
			List<String>  permissionCodes) {
		return permissionExtendBaseDao.deletePermission(entity, link, permissionCodes);
	}


	public boolean updatePermission(Object entity) {
		return permissionExtendBaseDao.updatePermission(entity);
	}

	public boolean updatePermission(Object entity, LogicOperator link) {
		return permissionExtendBaseDao.updatePermission(entity, link);
	}

	public boolean updatePermission(Object entity, LogicOperator link,
			List<String>  permissionCodes) {
		return permissionExtendBaseDao.updatePermission(entity, link, permissionCodes);
	}

	public boolean viewPermission(Object entity) {
		return permissionExtendBaseDao.viewPermission(entity);
	}

	public boolean viewPermission(Object entity, LogicOperator link) {
		return permissionExtendBaseDao.viewPermission(entity, link);
	}

	public boolean viewPermission(Object entity, LogicOperator link,
			List<String> permissionCodes) {
		return permissionExtendBaseDao.viewPermission(entity, link, permissionCodes);
	}

	public <T> void addPermissionCondition(Class<T> className,String hql, Object... values) {
		permissionExtendBaseDao.addPermissionCondition(className,hql,values);
	}

	public <T> void addPermissionCondition(Class<T> className,String hql, LogicOperator link,
			Object... values) {
		permissionExtendBaseDao.addPermissionCondition(className,hql, link, values);
	}

	public <T> void addPermissionCondition(Class<T> className,String hql, LogicOperator link,
			List<String> permissionCodes, Object... values) {
		permissionExtendBaseDao.addPermissionCondition(className,hql, link, permissionCodes, values);
	}
}
