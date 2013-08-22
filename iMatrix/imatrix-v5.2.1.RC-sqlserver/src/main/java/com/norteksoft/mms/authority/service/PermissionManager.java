package com.norteksoft.mms.authority.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.acs.service.authorization.StandardRoleManager;
import com.norteksoft.mms.authority.dao.PermissionDao;
import com.norteksoft.mms.authority.dao.PermissionItemDao;
import com.norteksoft.mms.authority.entity.DataRule;
import com.norteksoft.mms.authority.entity.Permission;
import com.norteksoft.mms.authority.entity.PermissionItem;
import com.norteksoft.mms.authority.enumeration.ItemType;
import com.norteksoft.mms.authority.enumeration.PermissionAuthorize;
import com.norteksoft.mms.base.utils.PermissionItemTreeUtil;
import com.norteksoft.mms.form.entity.TableColumn;
import com.norteksoft.mms.form.service.TableColumnManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Service
public class PermissionManager {
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private PermissionItemDao permissionItemDao;
	@Autowired
	private DataRuleManager dataRuleManager;
	@Autowired
	private TableColumnManager tableColumnManager;
	@Autowired
	private StandardRoleManager standardRoleManager;
	
	public void getPermissions(Page<Permission> page,Long dataRuleId){
		permissionDao.getPermissions(page, dataRuleId);
	}
	public void getPermissionsByMenuId(Page<Permission> page,Long menuId){
		permissionDao.getPermissionsByMenuId(page, menuId);
	}
	public Permission getPermissions(Long id){
		return permissionDao.get(id);
	}
	
	/**
	 * 保存数据授权
	 * @param permission
	 * @param auths
	 */
	public void savePermission(Permission permission,List<PermissionAuthorize> auths){
		//获得操作权限
		Integer permAuth=0;
		for(PermissionAuthorize auth:auths){
			permAuth=permAuth+auth.getCode();
		}
		permission.setAuthority(permAuth);
		permissionDao.save(permission);
		List<Object> list=JsonParser.getFormTableDatas(PermissionItem.class);
		List<PermissionItem> result=new ArrayList<PermissionItem>();
		for(Object obj:list){
			PermissionItem inter=(PermissionItem)obj;
			inter.setPermission(permission);
			permissionItemDao.save(inter);
			result.add(inter);
		}
		permission.setItems(result);
	}
	
	/**
	 * 删除数据授权
	 * @param ids
	 */
	public void deletePermissions(String ids){
		String[] idList=ids.split(",");
		for(String id:idList){
			if(StringUtils.isNotEmpty(id)){
				permissionDao.delete(Long.parseLong(id));
			}
		}
	}
	/**
	 * 根据数据规则获得数据授权列表
	 * @param dataRuleId
	 * @return
	 */
	public List<Permission> getPermissionsByDataRule(Long dataRuleId){
		return permissionDao.getPermissionsByDataRule(dataRuleId);
	}
	
	/**
	 * 验证该授权的优先级及操作权限
	 * @param auths
	 * @param dataRuleId
	 * @return 没有相同优先级和操作权限的返回true，否则返回false。返回true则可以保存
	 */
	public String validatePermission(String validateAuthCodes ,Long dataRuleId,Long permissionId,Integer priority ){
		String validateResult="";
		DataRule dataRule=dataRuleManager.getDataRule(dataRuleId);
		if(dataRule==null)return "true-保存";
		List<DataRule> result=dataRuleManager.getDataRuleByDataTable(dataRule.getDataTableId());
		String[] authCodes=validateAuthCodes.split(",");
		for(DataRule rule:result){
			List<Permission> permissions=getPermissionsByDataRule(rule.getId());
			for(Permission perm:permissions){
				if(!perm.getId().equals(permissionId)&&perm.getPriority().equals(priority)){//不是当前编辑的授权且优先级相等则做权限判断
					for(String authCode:authCodes){
						if(StringUtils.isNotEmpty(authCode)){
							PermissionAuthorize auth=getAuthByCode(Integer.parseInt(authCode));
							if((perm.getAuthority() & auth.getCode()) != 0){//有该权限
								validateResult="false-"+Struts2Utils.getText(auth.getI18nKey());
								return validateResult;
							}
						}
					}
				}
			}
		}
		validateResult="true-保存";
		return validateResult;
	}
	
	/**
	 * 根据权限编码获得操作权限
	 * @param code
	 * @return
	 */
	private PermissionAuthorize getAuthByCode(Integer code){
		for(PermissionAuthorize auth : PermissionAuthorize.values()){
			if(code.equals(auth.getCode())){
				return auth;
			}
		}
		return null;
	}
	
	/**
	 * 获取itemType树
	 * @param currentTreeId 
	 */
	public String getPermissionItemTree(Long id, ItemType itemType, String currentTreeId) {
		List<TableColumn> columns = getTableColumnsByPermissionId(id);
		List<Role> roles=standardRoleManager.getAllStandardRole(dataRuleManager.getDataRule(id).getSystemId());
		if(itemType==ItemType.USER || itemType==ItemType.CURRENT_USER_LOGINNAME || itemType==ItemType.CURRENT_USER_DIRECT_SUPERIOR_LOGINNAME){
			return PermissionItemTreeUtil.getUserAndFormTree(columns,currentTreeId);//获取用户和表单的树
		}else if( itemType==ItemType.CURRENT_USER_DEPARTMENT || itemType==ItemType.CURRENT_USER_SUPERIOR_DEPARTMENT|| itemType==ItemType.CURRENT_USER_TOP_DEPARTMENT || itemType==ItemType.CURRENT_USER_DIRECT_SUPERIOR_DEPARTMENT){
			return PermissionItemTreeUtil.getDepartmentAndFormTree(columns,currentTreeId,true);//获取部门和表单的树
		}else if(itemType==ItemType.ROLE || itemType==ItemType.CURRENT_USER_ROLE || itemType==ItemType.CURRENT_USER_DIRECT_SUPERIOR_ROLE){
			return PermissionItemTreeUtil.getRoleAndFormTree(columns,currentTreeId,roles);//获取角色和表单的树
		}else if(itemType==ItemType.WORKGROUP || itemType==ItemType.CURRENT_USER_WORKGROUP || itemType==ItemType.CURRENT_USER_DIRECT_SUPERIOR_WORKGROUP){
			return PermissionItemTreeUtil.getWorkgroupAndFormTree(columns,currentTreeId);//获取工作组和表单的树
		}else if(itemType==ItemType.DEPARTMENT){
			return PermissionItemTreeUtil.getDepartmentAndFormTree(columns,currentTreeId,false);//获取部门和表单的树
		}
		return null;
	}
	private List<TableColumn> getTableColumnsByPermissionId(Long id) {
		return tableColumnManager.getTableColumnByDataTableId(dataRuleManager.getDataRule(id).getDataTableId());
	}
	
}
