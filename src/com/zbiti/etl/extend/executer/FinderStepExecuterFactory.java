package com.zbiti.etl.extend.executer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.executer.ICommandExecuterFactory;
import com.zbiti.etl.core.smo.IFileTransferService;
import com.zbiti.etl.core.smo.IStepService;

/**
 * 
 * @author 严海平
 *
 */
@Service("finderStepExecuterFactory")
public class FinderStepExecuterFactory implements ICommandExecuterFactory<Boolean>{

	@Autowired
	IStepService stepService;
	@Autowired
	IFileTransferService fileTransferService;
	
	@Override
	public ICommandExecuter<Boolean> createExecuter() {
		FinderStepExecuter finderStepExecuter=new FinderStepExecuter();
		finderStepExecuter.setFileTransferService(fileTransferService);
		finderStepExecuter.setStepService(stepService);
		return finderStepExecuter;
	}

}
