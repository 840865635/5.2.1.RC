package com.norteksoft.task.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingMode;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.task.base.enumeration.TaskState;
import com.norteksoft.task.entity.HistoryWorkflowTask;

@Repository
public class HistoryWorkflowTaskDao extends HibernateDao<HistoryWorkflowTask, Long>{

	/**
	 * 根据用户获得自己所有已完成的流程名称
	 * @return
	 */
	public List<Object[]> getAllCompleteTaskGroupNames() {
		String hql="select t.groupName,count(t.groupName) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=? and t.paused=? and t.groupName!=null group by t.groupName";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已取消的流程名称
	 * @return
	 */
	public List<Object[]> getAllCancelTaskGroupNames(){
		String hql="select t.groupName,count(t.groupName) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=?) and t.paused=? and t.groupName!=null group by t.groupName";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已完成的流程名称
	 * @return
	 */
	public List<Object[]> getAllCompleteTaskCustomTypes() {
		String hql="select t.customType,count(t.customType) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=? and t.paused=? and t.customType!=null group by t.customType";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已取消的流程名称
	 * @return
	 */
	public List<Object[]> getAllCancelTaskCustomTypes() {
		String hql="select t.customType,count(t.customType) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=?) and t.paused=? and t.customType!=null group by t.customType";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已完成的任务类型名称
	 * @return
	 */
	public List<Object[]> getAllCompleteTaskTypeInfos() {
		String hql="select t.category,count(t.category) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=?  and t.paused=? and t.category!=null group by t.category";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已取消的任务类型名称
	 * @return
	 */
	public List<Object[]> getAllCancelTaskTypeInfos() {
		String hql="select t.category,count(t.category) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=?) and t.paused=? and t.category!=null group by t.category";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已完成的任务类型名称
	 * @return
	 */
	public Integer getAllCompleteTasksNum() {
		String hql="select count(t) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=?  and t.paused=?";
		Object obj=createQuery(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false).uniqueResult();
		if(obj!=null)return Integer.parseInt(obj.toString());
		return 0;
	}

	/**
	 * 根据用户获得自己所有已取消的任务类型名称
	 * @return
	 */
	public Integer getAllCancelTasksNum() {
		String hql="select count(t) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and ( t.active=? or t.active=?) and t.paused=?";
		Object obj=createQuery(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),false).uniqueResult();
		if(obj!=null)return Integer.parseInt(obj.toString());
		return 0;
	}

	/**
	 * 根据流程名称分页查询用户所有已完成任务
	 * @param page
	 * @param typeName
	 */
	public void getCompletedTasksByGroupName(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql=" from HistoryWorkflowTask t where  t.companyId = ? and t.transactor = ? and t.visible = true and t.active=?   and t.paused=? order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false);
		}else{
			hql=" from HistoryWorkflowTask t where   t.companyId = ? and t.transactor = ? and t.visible = true and t.active=?  and t.paused=? and t.groupName=?  order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false,typeName);
		}
	}

	/**
	 * 根据自定义类型分页查询用户所有已完成任务
	 * @param page
	 * @param typeName
	 */
	public void getCompletedTasksByCustomType(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql="select t from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and  t.active=?  and t.paused=? order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false);
		}else{
			hql="select t from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=?  and t.paused=? and t.customType=?  order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false,typeName);
		}
	}

	/**
	 * 分页查询用户已完成任务
	 * @param page
	 * @param typeName
	 */
	public void getCompletedTasksByUserType(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql="from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=? and t.paused=? and t.transactDate != null  order by t.transactDate desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(), TaskState.COMPLETED.getIndex(),false);
		}else{
			hql="from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=? and t.paused=? and t.category=? and t.transactDate != null  order by t.transactDate desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(), TaskState.COMPLETED.getIndex(),false,typeName);
		}
	}

	/**
	 * 根据流程名称分页查询用户所有已取消任务
	 * @param page
	 * @param typeName
	 */
	public void getCancelTasksByGroupName(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql=" from HistoryWorkflowTask t where  t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=? or t.active=?)   and t.paused=? order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false);
		}else{
			hql=" from HistoryWorkflowTask t where   t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=? or t.active=?)  and t.paused=? and t.groupName=?  order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,typeName);
		}
	}

	/**
	 * 根据自定义类型分页查询用户所有已取消任务
	 * @param page
	 * @param typeName
	 */
	public void getCancelTasksByCustomType(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql="select t from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and  (t.active=? or t.active=? or t.active=?)  and t.paused=? order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false);
		}else{
			hql="select t from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=? or t.active=?)  and t.paused=? and t.customType=?  order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,typeName);
		}
	}

	/**
	 * 分页查询用户已取消任务
	 * @param page
	 * @param typeName
	 */
	public void getCanceledTasksByUserType(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql="from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=? or t.active=?)  and t.paused=? order by t.transactDate desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false);
		}else{
			hql="from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=? or t.active=?)  and t.paused=? and t.category=? order by t.transactDate desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,typeName);
		}
	}
	
	public HistoryWorkflowTask getTask(Long taskId){
		return findUniqueNoCompanyCondition("from HistoryWorkflowTask t where t.id=?", taskId);
	}
	public HistoryWorkflowTask getTaskBySourceTaskId(Long taskId){
		return findUniqueNoCompanyCondition("from HistoryWorkflowTask t where t.sourceTaskId=?", taskId);
	}
	
	public List<String> getCountersignByProcessInstanceId(String processInstanceId,TaskProcessingMode processingMode){
		return find( "select distinct t.name from HistoryWorkflowTask t where t.processInstanceId=? and t.processingMode=? and t.paused=? ", processInstanceId,processingMode,false);
	}
	
	public List<HistoryWorkflowTask> getCountersignByProcessInstanceIdResult(String processInstanceId,String taskName,TaskProcessingResult result){
		return find( "from HistoryWorkflowTask t where t.processInstanceId=?  and t.name=? and t.taskProcessingResult=?  and t.paused=? ", processInstanceId,taskName,result,false);
	}
	/**
	 * 获得审批任务组数
	 * @param processInstanceId
	 * @param taskName
	 * @param result
	 * @return
	 */
	public List<Integer> getGroupNumByTaskName(String processInstanceId,String taskName){
		return find( "select t.groupNum from HistoryWorkflowTask t where t.processInstanceId=?  and t.name=?   and t.paused=? and t.companyId = ? group by t.groupNum", processInstanceId,taskName,false,ContextUtils.getCompanyId());
	}

	public void deleteHistoryTaskByProcessId(String processInstanceId, Long companyId) {
		this.createQuery("delete  from HistoryWorkflowTask t where  t.processInstanceId = ? and t.companyId = ? ", processInstanceId,companyId).executeUpdate();
	}
	
	public Page<HistoryWorkflowTask> getTaskAsTrustee(Long companyId, String loginName, Page<HistoryWorkflowTask> page,Boolean isEnd){
		Page<HistoryWorkflowTask> result = new Page<HistoryWorkflowTask>(page.getPageSize(), true);
		result.setPageNo(page.getPageNo());
		String hql = "from HistoryWorkflowTask t where t.companyId=? and t.transactor=? and t.visible = true and t.effective = true and (t.active=? or t.active=? or t.active=?  or t.active=?) and t.paused=? and t.trustor is not null";
		if(isEnd){
			this.findPage(result,hql,companyId, loginName, TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false);			
		}else{
			hql = "from HistoryWorkflowTask t where t.companyId=? and  t.transactor=?  and t.visible = true and t.effective = true and (t.active=? or t.active=? or t.active=? or t.active=?) and t.paused=? and t.trustor is not null";
			this.findPage(result,hql,companyId, loginName, TaskState.WAIT_TRANSACT.getIndex(), TaskState.WAIT_DESIGNATE_TRANSACTOR.getIndex(),TaskState.DRAW_WAIT.getIndex(),TaskState.WAIT_CHOICE_TACHE.getIndex(),false);
		}
		page = new Page<HistoryWorkflowTask>();
		page.setResult(result.getResult());
		page.setPageNo(result.getPageNo());
		page.setPageSize(result.getPageSize());
		page.setTotalCount(result.getTotalCount());
		return page;
	}
	public List<HistoryWorkflowTask> getWorkflowTasks(String instanceId, String taskName) {
		return find("from HistoryWorkflowTask t where t.processInstanceId = ? and t.name = ? and t.paused=?", instanceId, taskName,false);
	}


	public Page<HistoryWorkflowTask> getHistoryDelegateTasksByActive(
			Long companyId, String loginName,
			Page<HistoryWorkflowTask> historyTasks) {
		String hql = "from HistoryWorkflowTask t where t.companyId=? and t.trustor=?  and t.effective = true and (t.active=? or t.active=? or t.active=?  or t.active=?) and t.paused=?";
		return this.findPage(historyTasks,hql,companyId, loginName, TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false);
	}
	
	/**
	 * 查找公司中所有的超期任务,包括已完成的任务
	 * @param companyId
	 * @return
	 */
	public List<HistoryWorkflowTask> getTotalOverdueTasks(Long companyId) {
		 String hql = "from HistoryWorkflowTask t where t.companyId=? and  t.lastReminderTime is not null  and t.paused=? order by t.createdTime desc";
		return find(hql, companyId,false);
	}
	
	/**
	 * 查找当前办理人所有的超期任务的总数,包括已完成的任务
	 * @param companyId
	 * @param transactorName
	 * @return
	 */
	public Integer getTotalOverdueTasksNumByTransactor(Long companyId,String transactorName) {
		return Integer.parseInt(createQuery(
				"select count(t) from HistoryWorkflowTask t where t.companyId=? and t.transactor=? and t.lastReminderTime is not null  and t.paused=?", 
				companyId, transactorName,false).uniqueResult().toString());
	}
	
	/**
	 * 获得所有办理人除当前任务名称的办理人
	 * @param task
	 * @return
	 */
	public List<String> getTransactorsExceptTask(HistoryWorkflowTask task){
		String hql="select distinct t.transactor from HistoryWorkflowTask t where t.name!=? and t.companyId=? and t.processInstanceId=? and t.active=?  and t.paused=?";
		return this.find(hql, task.getName(),task.getCompanyId(),task.getProcessInstanceId(),TaskState.COMPLETED.getIndex(),false);
	}
	
	public List<String> getHandledTransactors(String workflowId) {
		String hql = "select t.transactor from HistoryWorkflowTask t where t.processInstanceId=? and t.active=? and t.effective=?  and t.paused=?";
		return find(hql, workflowId,TaskState.COMPLETED.getIndex(),true,false);
	}
	
	public Integer getTrusteeTasksNum(Long companyId, String loginName){
		String hql = "select count(t) from HistoryWorkflowTask t where t.companyId=?  and t.effective = true and t.transactor=? and (t.active=? or t.active=?  or t.active=?  or t.active=?) and t.paused=? and t.trustor is not null";
		Object o =  createQuery(hql, companyId, loginName, TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false).uniqueResult();
		return Integer.valueOf(o.toString());
	}
	
	public Integer getDelegateTasksNum(Long companyId, String loginName){
		String hql = "select count(t) from HistoryWorkflowTask t where t.companyId=? and t.effective = true and t.trustor=? and (t.active=? or t.active=?  or t.active=?  or t.active=?) and t.paused=?";
		Object o = createQuery(hql, companyId, loginName, TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false).uniqueResult();
		return Integer.valueOf(o.toString());
	}

	public List<HistoryWorkflowTask> getHistoryTasksByInstanceId(
			String processInstanceId) {
		return this.find("from HistoryWorkflowTask t where t.companyId = ? and t.processInstanceId = ? ", 
				ContextUtils.getCompanyId(), processInstanceId);
	}
}
