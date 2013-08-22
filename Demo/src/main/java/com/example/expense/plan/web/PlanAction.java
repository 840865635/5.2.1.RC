package com.example.expense.plan.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.expense.entity.Plan;
import com.example.expense.entity.PlanItem;
import com.example.expense.plan.service.PlanManager;
import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.acs.service.authorization.StandardRoleManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsTreeUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/plan")
@ParentPackage("default")
public class PlanAction extends CrudActionSupport<Plan> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Plan plan;
	private Page<Plan> page;
	private Page<PlanItem> pageItem;
	private String permissionFlag;
	@Autowired
	private PlanManager planManager;
	@Autowired
	private StandardRoleManager standardRoleManager;
	
	public Plan getModel() {
		return plan;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			plan=new Plan();
			plan.setCreatedTime(new Date());
		}else {
			plan=planManager.getPlan(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	
	public void prepareView() throws Exception {
		prepareModel();
	}
	@Action("view")
	public String view() throws Exception {
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		List<PlanItem> items=new ArrayList<PlanItem>();
		List<Object> objects=JsonParser.getFormTableDatas(PlanItem.class);
		for (Object obj : objects) {
			PlanItem planItem=(PlanItem)obj;
			planItem.setPlan(plan);
			items.add(planItem);
		}
		plan.setPlanItems(items);
		planManager.savePlan(plan);
		addActionMessage("<font class=\"onSuccess\"><nobr>保存成功！</nobr></font>");
		return "input";
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		addActionMessage("<font class=\"onSuccess\"><nobr>"+planManager.deletePlan(deleteIds)+"</nobr></font>");
		return "list";
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		page = planManager.list(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	@Action("list-plan-item")
	public String getItemDatas() throws Exception {
		pageItem = planManager.getPlanItemList(pageItem,id);
		renderText(PageUtils.pageToJson(pageItem));
		return null;
	}
	
	
	@Action("select-role")
	public String selectRole() throws Exception {
		StringBuilder tree=new StringBuilder();
		List<Role> roles=standardRoleManager.getAllStandardRole(ContextUtils.getSystemId("ems"));
		if(roles.size()<=0){
			tree.append(JsTreeUtils.generateJsTreeNodeNew("_role", "", "角色", ""));
		}else{
			tree.append(JsTreeUtils.generateJsTreeNodeNew("_role", "open", "角色",roles(roles) ,""));
		}
		this.renderText(tree.toString());
		return null;
	}
	
	private String roles(List<Role> roles){
		StringBuilder tree=new StringBuilder();
		for(Role role:roles){
			tree.append(JsTreeUtils.generateJsTreeNodeNew("role_"+role.getId()+"_"+role.getName(), "", role.getName(),"")).append(",");
		}
		JsTreeUtils.removeLastComma(tree);
		return tree.toString();
	}
	
	@Action("delete-planItem")
	public String deletePlanItem() throws Exception {
		planManager.deletePlanItem(id);
		String callback=Struts2Utils.getParameter("callback");
		this.renderText(callback+"({msg:'删除成功！'})");
		return null;
	}
	
	
	@Action("validate-permission")
	public String validatePermission(){
		boolean flag = false;
		plan=planManager.getPlan(id);
		if("view".equals(permissionFlag)){
			flag = planManager.getViewPermission(plan);
		}else if("update".equals(permissionFlag)){
			flag = planManager.getUpdatePermission(plan);
		}
		renderText(flag+"_"+id);
		return null;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Page<Plan> getPage() {
		return page;
	}

	public void setPage(Page<Plan> page) {
		this.page = page;
	}

	public Plan getPlan() {
		return plan;
	}

	public Page<PlanItem> getPageItem() {
		return pageItem;
	}

	public void setPageItem(Page<PlanItem> pageItem) {
		this.pageItem = pageItem;
	}

	public String getPermissionFlag() {
		return permissionFlag;
	}

	public void setPermissionFlag(String permissionFlag) {
		this.permissionFlag = permissionFlag;
	}

}
