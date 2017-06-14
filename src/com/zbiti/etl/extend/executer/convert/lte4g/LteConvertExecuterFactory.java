package com.zbiti.etl.extend.executer.convert.lte4g;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.executer.ICommandExecuterFactory;
import com.zbiti.etl.core.smo.IStepService;

@Service("lteConvertExecuterFactory")
public class LteConvertExecuterFactory  implements ICommandExecuterFactory<Boolean>{


	@Autowired
	IStepService stepService;
	
	@Override
	public ICommandExecuter<Boolean> createExecuter() {
		LteConvertExecuter cs=new LteConvertExecuter();
		cs.setStepService(stepService);
		return cs;
	}
}
