package com.zbiti.etl.extend.executer.convert.iposs_event_raw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.executer.ICommandExecuterFactory;
import com.zbiti.etl.core.smo.IStepService;

@Service("IpossEventRawConvertExecuterFactory")
public class IpossEventRawConvertExecuterFactory implements ICommandExecuterFactory<Boolean>{

	@Autowired
	IStepService stepService;
	
	@Override
	public ICommandExecuter<Boolean> createExecuter() {
		IpossEventRawConvertExecuter convertStepExecuter=new IpossEventRawConvertExecuter();
		convertStepExecuter.setStepService(stepService);
		return convertStepExecuter;
	}

}
