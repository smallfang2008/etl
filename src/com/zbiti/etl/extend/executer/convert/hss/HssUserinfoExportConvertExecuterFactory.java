package com.zbiti.etl.extend.executer.convert.hss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.executer.ICommandExecuterFactory;
import com.zbiti.etl.core.smo.IStepService;

@Service("HssUserinfoExportConvertExecuterFactory")
public class HssUserinfoExportConvertExecuterFactory implements ICommandExecuterFactory<Boolean>{

	@Autowired
	IStepService stepService;
	
	@Override
	public ICommandExecuter<Boolean> createExecuter() {
		HssUserinfoExportConvertExecuter convertStepExecuter=new HssUserinfoExportConvertExecuter();
		convertStepExecuter.setStepService(stepService);
		return convertStepExecuter;
	}

}
