package com.zbiti.etl.extend.executer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.executer.ICommandExecuterFactory;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.extend.smo.IKettleRunService;

/**
 * 
 * @author 严海平
 *
 */
@Service("KettleStepExecuterFactory")
public class KettleStepExecuterFactory implements ICommandExecuterFactory<Boolean>{

	@Autowired
	IStepService stepService;
	@Autowired
	IKettleRunService kettleRunService;
	
	@Override
	public ICommandExecuter<Boolean> createExecuter() {
		KettleStepExecuter kettleStepExecuter=new KettleStepExecuter();
		kettleStepExecuter.setStepService(stepService);
		kettleStepExecuter.setKettleRunService(kettleRunService);
		return kettleStepExecuter;
	}

}
