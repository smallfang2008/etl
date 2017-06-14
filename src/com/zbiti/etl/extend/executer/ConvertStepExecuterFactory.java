package com.zbiti.etl.extend.executer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.executer.ICommandExecuterFactory;
import com.zbiti.etl.core.smo.IStepService;

@Service("convertStepExecuterFactory")
public class ConvertStepExecuterFactory implements ICommandExecuterFactory<Boolean>{

	@Autowired
	IStepService stepService;
	
	@Override
	public ICommandExecuter<Boolean> createExecuter() {
		ConvertStepExecuter convertStepExecuter=new ConvertStepExecuter();
		convertStepExecuter.setStepService(stepService);
		return convertStepExecuter;
	}

}
