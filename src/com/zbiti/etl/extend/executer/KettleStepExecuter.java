package com.zbiti.etl.extend.executer;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.smo.IKettleRunService;
import com.zbiti.etl.extend.vo.KettleStep;

/**
 * 
 * @author 严海平
 * 
 */
public class KettleStepExecuter implements ICommandExecuter<Boolean> {
	private static final Log logger = LogFactory.getLog(KettleStepExecuter.class);
	
	
	@Override
	public Boolean execute(ApplicationContext ctx,Node node,Step step, Command command,
			IFileDescQueue fileDescQueue) throws Exception {
		logger.info("Kettle步骤-"+step.getStepName()+"["+step.getStepId()+"]开始执行!");
		KettleStep kettleStep=stepService.getKettleStep(step.getStepId());
		if(kettleStep==null)
			throw new Exception("kettle步骤("+step.getStepId()+")不存在!");
		if("0".equals(kettleStep.getKettleType()))
			kettleRunService.executeConvert(kettleStep.getKettleFileName(), kettleStep.getKettleFileStream());
		else if("1".equals(kettleStep.getKettleType()))
			kettleRunService.executeJob(kettleStep.getKettleFileName(), kettleStep.getKettleFileStream());
		else
			throw new Exception("kettle步骤("+step.getStepId()+")的步骤类型不是0和1!");
		return true;
	}
	
	
	

	IStepService stepService;
	IKettleRunService kettleRunService;
	public IStepService getStepService() {
		return stepService;
	}

	public void setStepService(IStepService stepService) {
		this.stepService = stepService;
	}

	public IKettleRunService getKettleRunService() {
		return kettleRunService;
	}

	public void setKettleRunService(IKettleRunService kettleRunService) {
		this.kettleRunService = kettleRunService;
	}


}
