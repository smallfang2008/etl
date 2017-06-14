package com.zbiti.etl.extend.executer;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zbiti.common.DBCommon;
import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IResourceService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Resource;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.vo.SqlStep;

/**
 * 
 * @author 严海平
 *
 */
public class SqlStepExecuter implements ICommandExecuter<Boolean>{

	protected static final Log logger=LogFactory.getLog(SqlStepExecuter.class);
	Connection connection = null;
	@Override
	public Boolean execute(ApplicationContext ctx,Node node,Step step,Command command,IFileDescQueue fileDescQueue) throws Exception {
		logger.info("SQL执行步骤-"+step.getStepName()+"["+step.getStepId()+"]开始执行!");
		try{
			logger.info("获取执行SQL步骤配置");
			SqlStep sqlStep=stepService.getSqlStepById(step.getStepId());
			logger.info("获取数据库["+sqlStep.getDbName()+"]资源");
			Resource resource=resourceService.getByNameCache(sqlStep.getDbName());
			String sqls=sqlStep.getSqls();
			logger.info("----------------------------SQL------------------------------\n"+sqls);
			logger.info("----------------------------SQL------------------------------");
			
			DBCommon dbCommon=new DBCommon(3, 3);
			logger.info("获取数据库连接");
			connection=dbCommon.getConnection(resource.getDriver(), resource.getUri(), resource.getUserName(), resource.getPassword());
			logger.info("执行SQL");
			dbCommon.execSqls(sqls, resource.getDriver(), connection);
		}catch(Exception ex){
			command.setExecInfo("执行SQL失败！"+ex.getMessage());
			throw ex;
		}

		return true;
	}

	
	private IStepService stepService;
	private IResourceService resourceService;
	public IStepService getStepService() {
		return stepService;
	}
	public void setStepService(IStepService stepService) {
		this.stepService = stepService;
	}
	public IResourceService getResourceService() {
		return resourceService;
	}
	public void setResourceService(IResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
}
