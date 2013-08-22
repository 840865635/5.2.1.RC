package com.norteksoft.mms.base.utils;

import java.util.ArrayList;
import java.util.List;

import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.mms.form.entity.TableColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Department;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.api.entity.Workgroup;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.tree.TreeAttr;
import com.norteksoft.product.util.tree.TreeNode;


/**
 * 数据授权获取ItemType树
 * @author Administrator
 *
 */
public class PermissionItemTreeUtil {
	
	/**
	 * 工作组和表单树
	 * @param columns
	 * @param currentTreeId
	 * @return
	 */
	public static String getWorkgroupAndFormTree(List<TableColumn> columns, String currentTreeId) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		List<Workgroup> workGroups = ApiFactory.getAcsService().getWorkgroups();
		TreeNode root = null;
		if(workGroups.size()>0){
			 root = new TreeNode(
						new TreeAttr("allWorkgroup","folder"), 
						"open", 
						"工作组");
		}else{
			 root = new TreeNode(
						new TreeAttr("allWorkgroup","folder"), 
						"", 
						"工作组");
		}
		if (currentTreeId.equals("0")) {
			root.setChildren(workgroupTree(workGroups));
			treeNodes.add(root);
		}
//		List<TreeNode> headFormNode = new ArrayList<TreeNode>();
//		TreeNode headFormContent = null;
//		headFormContent = new TreeNode(
//				new TreeAttr("allField","folder"), 	
//				"open",
//				"表单字段");
//		headFormContent.setChildren(formTree(columns));
//		headFormNode.add(headFormContent);
//		treeNodes.add(headFormContent);
		return JsonParser.object2Json(treeNodes);
	}

	private static List<TreeNode> workgroupTree(List<Workgroup> workGroups) {
		List<TreeNode> workGroupsTreeNodes = new ArrayList<TreeNode>();
		TreeNode workGroupsTreeContent = null;
		for (Workgroup workGroup : workGroups) {
			workGroupsTreeContent = new TreeNode(
			new TreeAttr("workgroup_"+workGroup.getId()+"_"+workGroup.getName(),"folder"), 	
			"",
			workGroup.getName());
			workGroupsTreeNodes.add(workGroupsTreeContent);
		}
		return workGroupsTreeNodes;
	}
	/**
	 * 角色和表单树
	 * @param columns
	 * @param currentTreeId
	 * @param roles
	 * @return
	 */
	public static String getRoleAndFormTree(List<TableColumn> columns, String currentTreeId,List<Role> roles) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		TreeNode root = new TreeNode(
		new TreeAttr("allRole","folder"), 
		"open", 
		"角色");
		if (currentTreeId.equals("0")) {
			root.setChildren(roleTree(roles));
			treeNodes.add(root);
		}
//		List<TreeNode> headFormNode = new ArrayList<TreeNode>();
//		TreeNode headFormContent = null;
//		headFormContent = new TreeNode(
//				new TreeAttr("allField","folder"), 	
//				"open",
//				"表单字段");
//		headFormContent.setChildren(formTree(columns));
//		headFormNode.add(headFormContent);
//		treeNodes.add(headFormContent);
		return JsonParser.object2Json(treeNodes);
	}

	private static List<TreeNode> roleTree(List<Role> roles) {
		List<TreeNode> roleTreeNodes = new ArrayList<TreeNode>();
		TreeNode rolesTreeContent = null;
		for (Role role : roles) {
			rolesTreeContent = new TreeNode(
			new TreeAttr("role_"+role.getId()+"_"+role.getName(),"folder"), 	
			"",
			role.getName());
			roleTreeNodes.add(rolesTreeContent);
		}
		return roleTreeNodes;
	}
	/**
	 * 部门和表单树
	 * @param columns
	 * @param currentTreeId
	 * @param showStandardField 
	 * @return
	 */
	public static String getDepartmentAndFormTree(List<TableColumn> columns, String currentTreeId, boolean showStandardField) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		TreeNode root = new TreeNode(
		new TreeAttr("allDepartment","folder"), 
		"open", 
		"部门");
		List<Department> departments = ApiFactory.getAcsService().getDepartments();
		if (currentTreeId.equals("0")) {
			root.setChildren(departmentTree(departments));
			treeNodes.add(root);
		}
//		List<TreeNode> headFormNode = new ArrayList<TreeNode>();
//		TreeNode headFormContent = null;
//		headFormContent = new TreeNode(
//				new TreeAttr("allField","folder"), 	
//				"closed",
//				"表单字段");
//		headFormContent.setChildren(formTree(columns));
//		headFormNode.add(headFormContent);
//		treeNodes.add(headFormContent);
		
		if(showStandardField){
			List<TreeNode> headStandardNode = new ArrayList<TreeNode>();
			TreeNode headStandardContent = null;
			headStandardContent = new TreeNode(
					new TreeAttr("allDepartment","folder"), 	
					"closed",
			"标准值");
			List<TreeNode> standardChild = new ArrayList<TreeNode>();
			standardChild.add(new TreeNode(
					new TreeAttr("standardDepartment_topDepartment_顶级部门","folder"), 	
					"",
			"顶级部门"));
			headStandardContent.setChildren(standardChild);
			headStandardNode.add(headStandardContent);
			treeNodes.add(headStandardContent);
		}
		return JsonParser.object2Json(treeNodes);
	}
	private static List<TreeNode> departmentTree(List<Department> departments){
		List<TreeNode> departmentNode = new ArrayList<TreeNode>();
		for (Department department : departments) {
			departmentNode.add(generatSubDeptNode(department));
		}
		return departmentNode;
	}
	private static TreeNode generatSubDeptNode(Department department){
		TreeNode departmentTreeContent = null;
		List<Department> subDepts = ApiFactory.getAcsService().getSubDepartmentList(department.getId());
		if(subDepts.isEmpty()){
				departmentTreeContent = new TreeNode(
				new TreeAttr("department_"+department.getId()+"_"+department.getName(),"folder"), 	
				"",
				department.getName());
		}else{
				List<TreeNode> subDepartmentNode = new ArrayList<TreeNode>();
				for(Department subDept : subDepts){
					subDepartmentNode.add(generatSubDeptNode(subDept));
				}
				departmentTreeContent = new TreeNode(
				new TreeAttr("department_"+department.getId()+"_"+department.getName(),"folder"), 	
				"closed",
				department.getName());
				departmentTreeContent.setChildren(subDepartmentNode);
		}
		return departmentTreeContent;
	}
	/**
	 * 获取用户和表单树
	 * @param columns
	 * @param currentTreeId 
	 * @return
	 */
	public static String getUserAndFormTree(List<TableColumn> columns, String currentTreeId) {
		List<Department> departments = ApiFactory.getAcsService().getDepartments();
		List<User> usersList = ApiFactory.getAcsService().getUsersWithoutDepartment();
		StringBuilder tree = new StringBuilder();
		String[] str = currentTreeId.split("_");
		//人员树
		if(currentTreeId.equals("0")){
			tree.append(getUserAndFormTree(usersList,departments,columns));
		}else if(str[0].equals("department")){
			tree.append(departmentTreeChange(Long.parseLong(str[1])));
		}
		return tree.toString();
	}
	private static String departmentTreeChange(Long departmentId){
		List<Department> childer = ApiFactory.getAcsService().getSubDepartmentList(departmentId);
		List<User> users = ApiFactory.getAcsService().getUsersByDepartmentId(departmentId);
	    List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		//加载此部门下用户
	    List<TreeNode> userTreeNode = usersTree(users);
	    //加载此部门下的子部门
	    List<TreeNode> childTreeNode = childerTreeChange(childer);
		
	    treeNodes.addAll(userTreeNode);
	    treeNodes.addAll(childTreeNode);
		return JsonParser.object2Json(treeNodes);
	}
	private static List<TreeNode> usersTree(List<User> usersList){
		List<TreeNode> usersTreeNodes = new ArrayList<TreeNode>();
		TreeNode userTreeContent = null;
		for (User user : usersList) {
			userTreeContent = new TreeNode(
					new TreeAttr("user_"+user.getLoginName()+"_"+user.getName(),"user"), 	
					"",
					user.getName());
					usersTreeNodes.add(userTreeContent);
		}
		return usersTreeNodes;
	}
	
	private static List<TreeNode> childerTreeChange(List<Department> childer){
		List<TreeNode> childenTreeNodes = new ArrayList<TreeNode>();
		TreeNode childTreeContent = null;
		for (Department department : childer) {
			List<User> users1 = ApiFactory.getAcsService().getUsersByDepartmentId(department.getId());
			if (users1 != null && users1.size() > 0) {
				List<Department> subDepts =ApiFactory.getAcsService().getSubDepartmentList(department.getId());
					childTreeContent = new TreeNode(
					new TreeAttr("department_"+department.getId()+"_"+department.getName(),"folder"), 	
					"closed",
					department.getName());
					//封装子节点
					List<TreeNode> childenSubTreeNodes = new ArrayList<TreeNode>();
					//子部门人员节点,必须加，否则树显示有问题
					 childenSubTreeNodes.addAll(usersTree(users1));
					 //递归
					childenSubTreeNodes.addAll(childerTreeChange(subDepts));
					childTreeContent.setChildren(childenSubTreeNodes);
					childenTreeNodes.add(childTreeContent);	
					
			}else {
				// 如果子部门下没有人员，则不显示(可以将下面代码注释)
				List<Department> subDepts =ApiFactory.getAcsService().getSubDepartmentList(department.getId());
					childTreeContent = new TreeNode(
					new TreeAttr("department_"+department.getId()+"_"+department.getName(),"folder"), 	
					"",
					department.getName());
					//封装子节点
					List<TreeNode> childenSubTreeNodes = new ArrayList<TreeNode>();
					//递归
					childenSubTreeNodes.addAll(childerTreeChange(subDepts));
					childTreeContent.setChildren(childenSubTreeNodes);
					childenTreeNodes.add(childTreeContent);	
			}
		}
		return childenTreeNodes;
	}
	/**
	 * 获取用户树
	 * @param usersList
	 * @param departments
	 * @return
	 */
	private static String getUserAndFormTree(List<User> usersList,
			List<Department> departments,List<TableColumn> columns) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		TreeNode dept = new TreeNode(
				new TreeAttr("allUser","folder"), 
				"open", 
				"组织结构");
		
		//封装部门子节点
		List<TreeNode> headDepartmentNode = new ArrayList<TreeNode>();
		//封装子节点
		List<TreeNode> childrenTreeNode = new ArrayList<TreeNode>();
		childrenTreeNode.addAll(departmentsTree(departments));
		//封装无部门人员节点
		TreeNode noDepartmentUserTreeContent = new TreeNode(
				new TreeAttr("department","folder"), 	
				"",
		"无部门人员");
		noDepartmentUserTreeContent.setChildren(usersNotInDepartment());
		childrenTreeNode.add(noDepartmentUserTreeContent);
		dept.setChildren(childrenTreeNode);
		headDepartmentNode.add(dept);
		treeNodes.add(dept);
		
//		List<TreeNode> headFormNode = new ArrayList<TreeNode>();
//		TreeNode headFormContent = null;
//		headFormContent = new TreeNode(
//				new TreeAttr("allField","folder"), 	
//				"open",
//				"表单字段");
//		headFormContent.setChildren(formTree(columns));
//		headFormNode.add(headFormContent);
//		treeNodes.add(headFormContent);
		
		List<TreeNode> togetherTreeNode = new ArrayList<TreeNode>();
		togetherTreeNode.addAll(headDepartmentNode);
		
		
		return JsonParser.object2Json(treeNodes);
	}
	
	private static List<TreeNode> formTree(List<TableColumn> columns) {
		List<TreeNode> formTreeNodes = new ArrayList<TreeNode>();
		TreeNode formTreeContent = null;
		for (TableColumn column : columns) {
			if(!column.getName().contains("$")){//子表字段不显示
				formTreeContent = new TreeNode(
						new TreeAttr("field_fieldValue-"+column.getName()+"_"+column.getAlias(),"folder"), 	
						"",
						column.getAlias());
				formTreeNodes.add(formTreeContent);
			}
		}
		return formTreeNodes;
	}

	/**
	 * 部门
	 * @param departments
	 * @return
	 */
	private static List<TreeNode> departmentsTree(List<Department> departments){
		List<TreeNode> departmentsTreeNode = new ArrayList<TreeNode>();
		TreeNode childTreeContent = null;
		for (Department department : departments) {
			List<Department> childer = ApiFactory.getAcsService().getSubDepartmentList(department.getId());
			List<User> users = ApiFactory.getAcsService().getUsersByDepartmentId(department.getId());
			if ((childer != null && childer.size() > 0|| users != null && users.size() > 0)) {
				childTreeContent = new TreeNode(
				new TreeAttr("department_"+department.getId(),"folder"), 	
				"closed",
				department.getName());
			}else{
				childTreeContent = new TreeNode(
				new TreeAttr("department_"+department.getId(),"folder"), 	
				"",
				department.getName());
			}
			departmentsTreeNode.add(childTreeContent);
		}
		return departmentsTreeNode;
	}
	 //无部门用户
	private static List<TreeNode> usersNotInDepartment(){
		List<User> usersList = ApiFactory.getAcsService().getUsersWithoutDepartment();
		List<TreeNode> usersTreeNodes = new ArrayList<TreeNode>();
		TreeNode userTreeContent = null;
		for (User user : usersList) {
			userTreeContent = new TreeNode(
			new TreeAttr("user_"+user.getId()+"_"+user.getName(),"user"), 	
			"",
			user.getName());
			usersTreeNodes.add(userTreeContent);
		}
		return usersTreeNodes;
	}

}
