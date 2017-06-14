package com.zbiti.etl.extend.executer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.executer.ICommandExecuterFactory;
import com.zbiti.etl.core.smo.IResourceService;
import com.zbiti.etl.core.smo.IStepService;

@Service("oracleLoadStepExecuterFactory")
public class OracleLoadStepExecuterFactory implements ICommandExecuterFactory<Boolean>{

	
	@Autowired
	IStepService stepService;
	@Autowired
	IResourceService resourceService;
	
	@Override
	public ICommandExecuter<Boolean> createExecuter() {
		OracleLoadStepExecuter oracleLoadStepExecuter=new OracleLoadStepExecuter();
		oracleLoadStepExecuter.setResourceService(resourceService);
		oracleLoadStepExecuter.setStepService(stepService);
		return oracleLoadStepExecuter;
	}

}
