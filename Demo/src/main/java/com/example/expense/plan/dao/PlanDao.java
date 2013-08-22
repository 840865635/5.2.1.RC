package com.example.expense.plan.dao;

import org.springframework.stereotype.Repository;

import com.example.expense.entity.Plan;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class PlanDao extends HibernateDao<Plan, Long> {
	public Page<Plan> list(Page<Plan> page){
		StringBuilder hql = new StringBuilder("select distinct plan from Plan plan inner join plan.planItems planItems ");
		ApiFactory.getDataPermissionService().addPermissionCondition(Plan.class,hql.toString());
		return searchPageByHql(page,hql.toString());
	}
	
	public Page<Plan> listWidgets(Page<Plan> page){
		return searchPageByHql(page,"from Plan plan where plan.creator=?",ContextUtils.getLoginName());
	}
}
