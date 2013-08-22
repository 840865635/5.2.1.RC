package com.norteksoft.product.api.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.norteksoft.acs.entity.organization.Company;
import com.norteksoft.product.api.ApiFactory;

public class Department  {
	private static final long serialVersionUID = 1L;

	private Long id;
	private boolean deleted;
	private Integer weight; 
	private String code;
	private String name;
	private String shortTitle;
	private String summary;
	private Company company;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public Department getParent(){
		if(this.id==null)return null;
		return ApiFactory.getAcsService().getParentDepartment(this.id);
	}
	public Set<Department> getChildren(){
		Set<Department> children = new HashSet<Department>(0);
		if(this.id==null) return children;
		List<Department> subDept = ApiFactory.getAcsService().getSubDepartmentList(this.id);
		if(subDept.size()>0)children.addAll(subDept);
		return children;
	}
	public boolean equals(Department department) {
		return department.getId().equals(this.id);
	}
}
