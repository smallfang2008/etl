package com.zbiti.etl.extend.executer;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zbiti.common.DBCommon;
import com.zbiti.common.RunShellUtil;
import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IResourceService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Resource;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.vo.ShellStep;

/**
 * 
 * @author 严海平
 *
 */
public class ShellStepExecuter implements ICommandExecuter<Boolean>{

	protected static final Log logger=LogFactory.getLog(SqlStepExecuter.class);
	Connection connection = null;
	@Override
	public Boolean execute(ApplicationContext ctx,Node node,Step step,Command command,IFileDescQueue fileDescQueue) throws Exception {
		logger.info("SHELL命令执行步骤-"+step.getStepName()+"["+step.getStepId()+"]开始执行!");
		try{
			logger.info("获取SHELL命令执行步骤配置");
			ShellStep shellStep=stepService.getShellStepCache(step.getStepId());
			logger.info("获取远程连接["+shellStep.getResourceName()+"]资源");
			Resource resource=resourceService.getByNameCache(shellStep.getResourceName());
			String shellCommands=shellStep.getShellCommands().replaceAll("\r", "");
			logger.info("----------------------------SHELL COMMAND START------------------------------\n"+shellCommands);
			logger.info("----------------------------SHELL COMMAND END------------------------------");
			
			logger.info("执行shell");
			RunShellUtil.cmdExcute(shellCommands, resource.getHostName(), resource.getUserName(), resource.getPassword());
		}catch(Exception ex){
			command.setExecInfo("执行SHELL失败！"+ex.getMessage());
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
