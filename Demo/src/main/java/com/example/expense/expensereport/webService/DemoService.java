package com.example.expense.expensereport.webService;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.expense.base.utils.ExpenseThread;
import com.example.expense.entity.ExpenseReport;
import com.example.expense.expensereport.service.ExpenseReportManager;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.util.ThreadPool;

@Component//spring注入
@Path("/ems")
public class DemoService{
	@Autowired
	private AcsUtils acsUtils;
	
	
	@Autowired
	private ExpenseReportManager expenseReportManager;
	/**
	  * 根据与客户确认情况，用户登录使用用户名、密码方式进行登录。用户认证接口暂定义为
	  * @param 	account    输入参数    用户登录时在鼎盾打印客户端输入的用户帐户
	  * @param 	password   输入参数    用户登录时在鼎盾打印客户端输入的用户密码
	  * Md5.toMessageDigest(password))
	  */
	@POST
	@Path("/dispatchReport")
	@Produces("text/html;charset=UTF-8")
	@Consumes("text/html;charset=UTF-8")
	public Response dispatchReport(@FormParam("runAsUser")String identity) {
		
		ExpenseReport expenseReport = new ExpenseReport();
		expenseReport.setCreator(identity);
		expenseReport.setCompanyId(acsUtils.getCompanyIdLoginName(identity));
		expenseReport.setCreatedTime(new Date());
		expenseReport.setName(acsUtils.getUserByLoginName(identity).getName());
		expenseReport.setMoney(1000l);
		expenseReport.setInvoiceAmount(10);
		expenseReport.setFirstLoginName(identity);
		expenseReport.setSignLoginNames(identity);
		expenseReport.setReadLoginNames(identity);
		
		ExpenseThread thread = new ExpenseThread();
		thread.setExpenseReport(expenseReport);
		thread.setCompanyId(acsUtils.getCompanyIdLoginName(identity));
		thread.setLoginName(identity);
		thread.setUserName(acsUtils.getUserByLoginName(identity).getName());
		thread.setExpenseReportManager(expenseReportManager);
		ThreadPool.execute(thread);
		return Response.status(200).entity("ok").build();
	}
}
