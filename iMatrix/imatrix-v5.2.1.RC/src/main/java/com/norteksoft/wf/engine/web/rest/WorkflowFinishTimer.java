package com.norteksoft.wf.engine.web.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.util.ThreadPool;
import com.norteksoft.wf.base.utils.WorkflowFinishThread;
import com.norteksoft.wf.engine.service.WorkflowFinishManager;

@Component
@Path("/workflow")
public class WorkflowFinishTimer {
	@Autowired
	private AcsUtils acsUtils;
	@Autowired
	private WorkflowFinishManager workflowFinishManager;
	
	@POST
	@Path("/finish")
	@Produces("text/html;charset=UTF-8")
	@Consumes("text/html;charset=UTF-8")
	public Response userAuthentication(@FormParam("runAsUser")String identity) {
		try {
			WorkflowFinishThread thread = new  WorkflowFinishThread();
			thread.setCompanyId(acsUtils.getCompanyIdLoginName(identity));
			thread.setLoginName(identity);
			thread.setUserName(acsUtils.getUserByLoginName(identity).getName());
			thread.setWorkflowFinishManager(workflowFinishManager);
			ThreadPool.execute(thread);
		} catch (Exception e) {
			return Response.status(201).entity(e.getMessage()).build();
		}
		return Response.status(201).entity(" workflow finish ok ").build();
	}
}
